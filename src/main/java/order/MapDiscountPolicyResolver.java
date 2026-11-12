package order;

import java.util.Map;
import java.util.Objects;

/**
 * 등급 문자열과 정책 객체의 등록 정보를 이용해 정책을 찾는 실제 구현체입니다.
 * OrderApplication은 실제 실행용으로, 통합 테스트는 검증용으로 이 객체를 주입합니다.
 */

public class MapDiscountPolicyResolver implements DiscountPolicyResolver {
    //이게 바로 다형성의 예시.
    //Vip,Gold.Fixed 할인정책 모두 DiscountPolicy 라는 공통 인터페이스를 사용하기 때문에 이게 가능
    private final Map<String, DiscountPolicy> policies;

    //1.Objects.requireNonNull()
    //전달받은 policies가 null이면 NullPointerException을 발생시킨다. policies는 오류메세지
    //2.Map.copyOf()
    //전달받은 Map을 바탕으로 수정할 수 없는 Map을 만든다. (방어적 복사)
    //이렇게 하면 외부에서 원래 Map을 변경하더라도 Resolver에 등록된 정책 정보는 영향을 받지 않는다.
    public MapDiscountPolicyResolver(Map<String, DiscountPolicy> policies) {
        this.policies = Map.copyOf(Objects.requireNonNull(policies, "policies"));
    }

    @Override
    public DiscountPolicy resolve(String grade) {
        // TODO: grade가 null이거나 policies에 없으면 UnknownGradeException(grade)을 발생시키세요.
        // TODO: policies에서 등급에 맞는 정책을 찾아 반환하세요.
        if(grade==null){
            throw new UnknownGradeException(grade);
        }

        //위에서 만든 Map에서 get으로 등급에 해당하는 정책 가져오기.
        DiscountPolicy policy = policies.get(grade);

        if (policy==null){
            throw new UnknownGradeException(grade);
        }

        return policy;
    }
}
