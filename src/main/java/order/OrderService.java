package order;

import java.util.List;
import java.util.Objects;

/** 주문 상품을 검증하고 합계와 할인 결과를 계산하는 서비스입니다. */
public class OrderService {
    // 구체적인 정책 클래스가 아닌 정책 선택 규약을 생성자로 받습니다.
    //나중에 테스트를 하기 쉽게 의존성을 주입한 건가요?
    private final DiscountPolicyResolver policyResolver;

    public OrderService(DiscountPolicyResolver policyResolver) {
        this.policyResolver = Objects.requireNonNull(policyResolver, "policyResolver");
    }

    public OrderResult order(String grade, List<OrderItem> items) {
        // TODO: null/빈 목록과 null 상품은 InvalidOrderException으로 거절하세요.
        
        //하나로 묶어서 처리 , 왼쪽부터 검사하기에 오류가 나지 않음
        if(items == null || items.isEmpty()){
            throw new InvalidOrderException();
        }
        // TODO: 음수 단가는 InvalidPriceException, 1 미만 수량은 InvalidQuantityException으로 거절하세요.

        // TODO: 각 상품의 단가 × 수량을 합산해 주문 금액을 long으로 계산하세요.

        long totalPrice = 0;

        for(OrderItem item : items){
            if (item == null) {
                throw new InvalidOrderException();
            }
            if (item.unitPrice() < 0) {
                throw new InvalidPriceException();
            }
            if (item.quantity() < 1) {
                throw new InvalidQuantityException();
            }
            totalPrice += (long)item.unitPrice() * item.quantity();
        }

        // TODO: 모든 상품 검증이 끝난 다음 policyResolver로 정책을 선택하고 할인 금액을 계산하세요.



        // TODO: 할인 금액이 음수이거나 주문 금액을 초과하면 IllegalStateException을 발생시키세요.

        DiscountPolicy policy = policyResolver.resolve(grade);
        long discountedPrice = policy.discount(totalPrice);

        if(discountedPrice < 0 || discountedPrice > totalPrice){
            throw new IllegalStateException();
        }

        // TODO: 입력을 수정하거나 출력하지 말고 주문/할인/결제 금액을 OrderResult로 반환하세요.
        return new OrderResult(totalPrice,discountedPrice,totalPrice-discountedPrice);
        // 구현을 마치면 아래 임시 예외를 삭제하세요.
    }
}
