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
        if (grade == null) {
            throw new UnknownGradeException(grade);
        }
        DiscountPolicy policy = policies.get(grade);
        if (policy == null) {
            throw new UnknownGradeException(grade);
        }
        return policy;
    }
}
