package order;

/** 등급에 맞는 할인 정책을 선택합니다. 주문 서비스는 정책 등록 방식을 알 필요가 없습니다. */
public interface DiscountPolicyResolver {
    /** @throws UnknownGradeException 등록되지 않은 등급인 경우 */
    DiscountPolicy resolve(String grade);
}
