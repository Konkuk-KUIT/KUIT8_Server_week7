package order;

/** GOLD 등급의 할인 금액을 계산합니다. */
public class GoldDiscountPolicy implements DiscountPolicy {
    @Override
    public long discount(long orderAmount) {
        // TODO: README의 GOLD 할인율과 정수 나눗셈 규칙을 적용하세요.
        return (long)(orderAmount*0.9);
    }
}
