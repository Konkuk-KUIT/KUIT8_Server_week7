package order;

/** VIP 등급의 할인 금액을 계산합니다. */
public class VipDiscountPolicy implements DiscountPolicy {
    @Override
    public long discount(long orderAmount) {
        // TODO: README의 VIP 할인율과 정수 나눗셈 규칙을 적용하세요.
        // VIP 정책은 주문 금액의 20%를 할인하는 정책
        return orderAmount*20/100;
    }
}
