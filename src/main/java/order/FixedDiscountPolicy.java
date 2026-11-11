package order;

/** 주문 금액과 관계없이 정해진 금액을 할인하는 정책입니다. */
public class FixedDiscountPolicy implements DiscountPolicy {
    private static final long FIXED_DISCOUNT = 1_000L;
    @Override
    public long discount(long orderAmount) {
        return Math.min(orderAmount, FIXED_DISCOUNT);
    }
}
