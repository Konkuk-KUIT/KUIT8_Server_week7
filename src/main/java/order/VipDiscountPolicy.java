package order;

/** VIP 등급의 할인 금액을 계산합니다. */
public class VipDiscountPolicy implements DiscountPolicy {
    @Override
    public long discount(long orderAmount) {
        return orderAmount / 5;
    }
}
