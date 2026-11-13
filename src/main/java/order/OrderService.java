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
        // TODO: null/빈 목록과 null 상품은 InvalidOrderException으로 거절하세요.
        if (items==null || items.isEmpty()){
            throw new InvalidOrderException();
        }

        long orderAmount = 0L;

        // TODO: 음수 단가는 InvalidPriceException, 1 미만 수량은 InvalidQuantityException으로 거절하세요.
        for (OrderItem item : items) {
            // null 상품 검증
            if (item == null) {
                throw new InvalidOrderException();
            }

            if (item.unitPrice() < 0) {
                throw new InvalidPriceException();
            }

            if (item.quantity() < 1) {
                throw new InvalidQuantityException();
            }

            // TODO: 각 상품의 단가 × 수량을 합산해 주문 금액을 long으로 계산하세요.

            orderAmount += (long) item.unitPrice() * item.quantity();

        }
        // TODO: 모든 상품 검증이 끝난 다음 policyResolver로 정책을 선택하고 할인 금액을 계산하세요.
        long discountAmount= policyResolver.resolve(grade).discount(orderAmount);

        // TODO: 할인 금액이 음수이거나 주문 금액을 초과하면 IllegalStateException을 발생시키세요.
        if (discountAmount < 0 || orderAmount < discountAmount) {
            throw new IllegalStateException();
        }
        long finalPrice = orderAmount - discountAmount;
        // TODO: 입력을 수정하거나 출력하지 말고 주문/할인/결제 금액을 OrderResult로 반환하세요.
        return new OrderResult(orderAmount, discountAmount,finalPrice);

        // 구현을 마치면 아래 임시 예외를 삭제하세요.
    }
}
