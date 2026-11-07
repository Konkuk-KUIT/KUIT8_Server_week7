package order;

/** 검증과 할인 계산이 끝난 주문의 금액을 담습니다. 세 값의 단위는 원입니다. */
public record OrderResult(long orderAmount, long discountAmount, long finalPrice) {
}
