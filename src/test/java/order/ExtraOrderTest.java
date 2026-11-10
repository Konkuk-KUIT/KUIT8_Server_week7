package order;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** 공개 테스트 외에 직접 추가한 테스트*/
public class ExtraOrderTest {

    @Test
    void newGradeCanBeAddedWithoutChangingOrderService() {
        // given: OrderService 코드는 그대로 두고, 새 등급 SILVER(5% 할인) 정책만 새로 등록
        DiscountPolicy silverPolicy = orderAmount -> orderAmount * 5 / 100;
        OrderService service = new OrderService(
                new MapDiscountPolicyResolver(Map.of("SILVER", silverPolicy)));

        // when
        OrderResult result = service.order("SILVER", List.of(new OrderItem("책", 10_000, 1)));

        // then: 10,000원의 5% = 500원 할인, 9,500원 결제
        assertThat(result).isEqualTo(new OrderResult(10_000, 500, 9_500));
    }

    @Test
    void fixedDiscountAtExactlyOneThousand() {
        // 경계값: 정확히 1,000원 주문이면 1,000원 할인합니다.
        assertThat(new FixedDiscountPolicy().discount(1_000)).isEqualTo(1_000);
    }

    @Test
    void resolverIsNotAffectedByChangesToOriginalMap() {
        // given: 원본 Map으로 resolver를 만든 뒤
        Map<String, DiscountPolicy> policies = new HashMap<>();
        policies.put("VIP", new VipDiscountPolicy());
        MapDiscountPolicyResolver resolver = new MapDiscountPolicyResolver(policies);

        // when: 원본 Map에 나중에 GOLD를 추가해도
        policies.put("GOLD", new GoldDiscountPolicy());

        // then: resolver는 생성 시점에 복사해 둔 정책만 사용합니다. (방어적 복사)
        assertThat(resolver.resolve("VIP")).isInstanceOf(VipDiscountPolicy.class);
        assertThatThrownBy(() -> resolver.resolve("GOLD"))
                .isInstanceOf(UnknownGradeException.class);
    }
}