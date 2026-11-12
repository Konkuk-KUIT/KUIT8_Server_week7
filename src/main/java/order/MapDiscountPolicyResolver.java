package order;

import java.util.Map;
import java.util.Objects;

/**
 * 등급 문자열과 정책 객체의 등록 정보를 이용해 정책을 찾는 실제 구현체입니다.
 * OrderApplication은 실제 실행용으로, 통합 테스트는 검증용으로 이 객체를 주입합니다.
 */
public class MapDiscountPolicyResolver implements DiscountPolicyResolver {
    private final Map<String, DiscountPolicy> policies;

    public MapDiscountPolicyResolver(Map<String, DiscountPolicy> policies) {
        this.policies = Map.copyOf(Objects.requireNonNull(policies, "policies"));
    }

    @Override
    public DiscountPolicy resolve(String grade) {
        // TODO: grade가 null이거나 policies에 없으면 UnknownGradeException(grade)을 발생시키세요.
        // TODO: policies에서 등급에 맞는 정책을 찾아 반환하세요.
        // 구현을 마치면 아래 임시 예외를 삭제하세요.
        if(grade == null ||  policies.containsKey(grade)) throw new UnknownGradeException(grade);
        return policies.get(grade);
    }
}
