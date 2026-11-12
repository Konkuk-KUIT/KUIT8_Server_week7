package order;

/** VIP 등급의 할인 금액을 계산합니다. */
public class VipDiscountPolicy implements DiscountPolicy {
    @Override
    public long discount(long orderAmount) {
        // TODO: README의 VIP 할인율과 정수 나눗셈 규칙을 적용하세요.
        return (long)(orderAmount*0.8);
    }
}
