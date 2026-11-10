package order;

import java.util.List;
import java.util.Objects;

/** 주문 상품을 검증하고 합계와 할인 결과를 계산하는 서비스입니다. */
public class OrderService {
    // 구체적인 정책 클래스가 아닌 정책 선택 규약을 생성자로 받습니다.
    private final DiscountPolicyResolver policyResolver;

    public OrderService(DiscountPolicyResolver policyResolver) {
        this.policyResolver = Objects.requireNonNull(policyResolver, "policyResolver");
    }

    public OrderResult order(String grade, List<OrderItem> items) {
        validateItems(items); // 1. 입력 검증 (전부 끝까지)
        long orderAmount = calculateOrderAmount(items); // 2. 합계 계산

        DiscountPolicy policy = policyResolver.resolve(grade); // 3. 정책 선택
        long discountAmount = policy.discount(orderAmount); // 4. 할인 계산
        validateDiscount(orderAmount, discountAmount); // 5. 할인액 검증

        return new OrderResult(orderAmount, discountAmount, orderAmount - discountAmount);
    }

    private void validateItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException();
        }
        for (OrderItem item : items) {
            if (item == null) { //장바구니가 텅 빔
                throw new InvalidOrderException();
            }
            if (item.unitPrice() < 0) { //가격이 음수
                throw new InvalidPriceException();
            }
            if (item.quantity() < 1) { //물건 0개 구매
                throw new InvalidQuantityException();
            }
        }
    }

    //물건값 더하기
    private long calculateOrderAmount(List<OrderItem> items) {
        long orderAmount = 0;
        for (OrderItem item : items) {
            orderAmount += (long) item.unitPrice() * item.quantity();
        }
        return orderAmount;
    }

    //할인 받기
    private void validateDiscount(long orderAmount, long discountAmount) {
        if (discountAmount < 0 || discountAmount > orderAmount) {
            throw new IllegalStateException("할인 정책이 잘못된 할인 금액을 반환했습니다: " + discountAmount);
        }
    }
}
