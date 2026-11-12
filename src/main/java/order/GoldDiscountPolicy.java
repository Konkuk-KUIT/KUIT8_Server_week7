package order;

/** GOLD 등급의 할인 금액을 계산합니다. */
public class GoldDiscountPolicy implements DiscountPolicy {
    @Override
    public long discount(long orderAmount) {
        return orderAmount * 10 / 100;
    }
}
