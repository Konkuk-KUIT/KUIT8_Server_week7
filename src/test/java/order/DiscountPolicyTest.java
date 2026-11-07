package order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** 각 정책이 돌려주는 '할인 금액'을 주문 서비스와 분리해서 검사합니다. */
class DiscountPolicyTest {
    @Test
    void vipDiscountsTwentyPercent() {
        // 25,000원의 20%는 5,000원입니다.
        assertThat(new VipDiscountPolicy().discount(25_000)).isEqualTo(5_000);
    }

    @Test
    void goldDiscountTruncatesFraction() {
        // 9,999원의 10%는 999.9원이므로 원 단위에서 소수 부분을 버립니다.
        assertThat(new GoldDiscountPolicy().discount(9_999)).isEqualTo(999);
    }

    @Test
    void fixedDiscountNeverExceedsOrderAmount() {
        DiscountPolicy policy = new FixedDiscountPolicy();

        // 일반 주문, 1,000원 미만 주문, 0원 주문을 함께 확인합니다.
        assertThat(policy.discount(25_000)).isEqualTo(1_000);
        assertThat(policy.discount(700)).isEqualTo(700);
        assertThat(policy.discount(0)).isZero();
    }
}
