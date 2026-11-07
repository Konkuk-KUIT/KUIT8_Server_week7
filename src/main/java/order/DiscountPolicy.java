package order;

/** 주문 금액을 받아 할인 금액을 계산하는 정책의 공통 규약입니다. */
public interface DiscountPolicy {
    /** @return 최종 결제 금액이 아닌 할인 금액(원) */
    long discount(long orderAmount);
}
