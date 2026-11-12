package order;

import java.util.List;
import java.util.Objects;

/**
 * 주문 상품을 검증하고 합계와 할인 결과를 계산하는 서비스입니다.
 */
public class OrderService {
    // 구체적인 정책 클래스가 아닌 정책 선택 규약을 생성자로 받습니다.
    private final DiscountPolicyResolver policyResolver;

    public OrderService(DiscountPolicyResolver policyResolver) {
        this.policyResolver = Objects.requireNonNull(policyResolver, "policyResolver");
    }

    public OrderResult order(String grade, List<OrderItem> items) {

        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException();
        }
        long total = 0;
        for (OrderItem item : items) {
            if (item == null) {
                throw new InvalidOrderException();
            }

            if (item.unitPrice() < 0) {
                throw new InvalidPriceException();
            }

            if (item.quantity() <= 0) {
                throw new InvalidQuantityException();
            }

            total += (long) item.unitPrice() * item.quantity();
        }

        DiscountPolicy discountPolicy = policyResolver.resolve(grade);

        long discountPrice = discountPolicy.discount(total);
        if (discountPrice < 0 || discountPrice > total) {
            throw new IllegalStateException();
        }
        return new OrderResult(total, discountPrice, total - discountPrice);
    }
}
