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
        // TODO: 음수 단가는 InvalidPriceException, 1 미만 수량은 InvalidQuantityException으로 거절하세요.
        // TODO: 각 상품의 단가 × 수량을 합산해 주문 금액을 long으로 계산하세요.
        // TODO: 모든 상품 검증이 끝난 다음 policyResolver로 정책을 선택하고 할인 금액을 계산하세요.
        // TODO: 할인 금액이 음수이거나 주문 금액을 초과하면 IllegalStateException을 발생시키세요.
        // TODO: 입력을 수정하거나 출력하지 말고 주문/할인/결제 금액을 OrderResult로 반환하세요.
        // 구현을 마치면 아래 임시 예외를 삭제하세요.
         // 1. 주문 목록 검증
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException();
        }

        // 2. 각 상품 검증 및 총 주문 금액 계산
        long orderAmount = 0L;

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

            orderAmount += (long) item.unitPrice() * item.quantity();
        }

        // 3. 회원 등급에 맞는 할인 정책 선택
        DiscountPolicy policy = policyResolver.resolve(grade);

        // 4. 할인 금액 계산
        long discountAmount = policy.discount(orderAmount);

        // 5. 할인 금액의 유효성 검증
        if (discountAmount < 0 || discountAmount > orderAmount) {
            throw new IllegalStateException("Invalid discount amount");
        }

        // 6. 최종 주문 결과 반환
        return new OrderResult(
            orderAmount,
            discountAmount,
            orderAmount - discountAmount
        );
    }
}
