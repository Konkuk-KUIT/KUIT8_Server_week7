package order;

/** 주문 금액과 관계없이 정해진 금액을 할인하는 정책입니다. */
public class FixedDiscountPolicy implements DiscountPolicy {
    private static final int FIXED_DISCOUNT_COST = 1000;
    @Override
    public long discount(long orderAmount) {
        // TODO: 소액 주문과 0원 주문에서도 할인액이 주문 금액을 넘지 않게 하세요.
        return Math.max(orderAmount - FIXED_DISCOUNT_COST,FIXED_DISCOUNT_COST);
    }
}
