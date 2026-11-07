package order;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/** 주문 계산 결과와 주입된 협력 객체의 호출 순서를 검증합니다. */
class OrderServiceTest {
    // mock은 실제 정책 계산 없이 주문 서비스의 책임만 확인할 때 사용합니다.
    private final DiscountPolicyResolver resolver = mock(DiscountPolicyResolver.class);
    private final OrderService service = new OrderService(resolver);

    @Test
    void calculatesMultipleItemsAndUsesInjectedPolicy() {
        // given: VIP 정책이 주문 금액 25,000원에 5,000원을 할인하도록 설정합니다.
        DiscountPolicy policy = mock(DiscountPolicy.class);
        when(resolver.resolve("VIP")).thenReturn(policy);
        when(policy.discount(25_000)).thenReturn(5_000L);
        List<OrderItem> items = new ArrayList<>(List.of(
                new OrderItem("책", 10_000, 2),
                new OrderItem("펜", 5_000, 1)
        ));
        List<OrderItem> originalItems = List.copyOf(items);

        // when
        OrderResult result = service.order("VIP", items);

        // then: 금액뿐 아니라 올바른 협력 객체를 호출했는지도 확인합니다.
        assertThat(result).isEqualTo(new OrderResult(25_000, 5_000, 20_000));
        assertThat(items).containsExactlyElementsOf(originalItems);
        verify(resolver).resolve("VIP");
        verify(policy).discount(25_000);
    }

    @Test
    void rejectsNegativePriceBeforeSelectingPolicy() {
        // 유효하지 않은 주문은 정책을 찾기 전에 거절해야 합니다.
        assertThatThrownBy(() -> service.order("VIP", List.of(new OrderItem("책", -1, 1))))
                .isInstanceOf(InvalidPriceException.class);
        verifyNoInteractions(resolver);
    }

    @Test
    void rejectsNonPositiveQuantityBeforeSelectingPolicy() {
        // 수량은 1개 이상이어야 하므로 0과 음수를 모두 거절합니다.
        assertThatThrownBy(() -> service.order("VIP", List.of(new OrderItem("책", 100, 0))))
                .isInstanceOf(InvalidQuantityException.class);
        assertThatThrownBy(() -> service.order("VIP", List.of(new OrderItem("책", 100, -1))))
                .isInstanceOf(InvalidQuantityException.class);
        verifyNoInteractions(resolver);
    }

    @Test
    void validatesAllItemsBeforeSelectingPolicy() {
        // 앞 상품이 정상이더라도 뒤 상품이 잘못되면 정책 선택 전에 거절합니다.
        List<OrderItem> items = new ArrayList<>(List.of(
                new OrderItem("책", 10_000, 1),
                new OrderItem("펜", 500, 0)
        ));
        List<OrderItem> originalItems = List.copyOf(items);

        assertThatThrownBy(() -> service.order("VIP", items))
                .isInstanceOf(InvalidQuantityException.class);
        assertThat(items).containsExactlyElementsOf(originalItems);
        verifyNoInteractions(resolver);
    }

    @Test
    void rejectsEmptyAndNullOrdersBeforeSelectingPolicy() {
        // 상품이 없는 주문과 목록 자체가 없는 주문을 모두 확인합니다.
        assertThatThrownBy(() -> service.order("VIP", List.of()))
                .isInstanceOf(InvalidOrderException.class);
        assertThatThrownBy(() -> service.order("VIP", null))
                .isInstanceOf(InvalidOrderException.class);
        verifyNoInteractions(resolver);
    }

    @Test
    void rejectsNullItemBeforeSelectingPolicy() {
        // List.of는 null을 허용하지 않아 ArrayList로 잘못된 입력을 만듭니다.
        List<OrderItem> items = new ArrayList<>();
        items.add(null);
        assertThatThrownBy(() -> service.order("VIP", items))
                .isInstanceOf(InvalidOrderException.class);
        verifyNoInteractions(resolver);
    }

    @Test
    void rejectsInvalidDiscountFromCollaborator() {
        // 정책이 잘못된 할인액을 돌려주더라도 음수 결제 금액을 반환하면 안 됩니다.
        DiscountPolicy policy = mock(DiscountPolicy.class);
        when(resolver.resolve("VIP")).thenReturn(policy);
        when(policy.discount(100)).thenReturn(101L);

        assertThatThrownBy(() -> service.order("VIP", List.of(new OrderItem("책", 100, 1))))
                .isInstanceOf(IllegalStateException.class);

        // 할인액은 0원 이상이어야 하므로 음수 할인액도 거절합니다.
        when(policy.discount(100)).thenReturn(-1L);
        assertThatThrownBy(() -> service.order("VIP", List.of(new OrderItem("책", 100, 1))))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void integratesAllPoliciesAndUnknownGrade() {
        // 이 테스트가 실행 시작점처럼 실제 정책을 등록하고 서비스에 주입합니다.
        // 위 단위 테스트의 Mockito mock과 달리, 아래 resolver는 실제 구현체입니다.
        MapDiscountPolicyResolver resolver = new MapDiscountPolicyResolver(Map.of(
                "VIP", new VipDiscountPolicy(),
                "GOLD", new GoldDiscountPolicy(),
                "FIXED", new FixedDiscountPolicy()
        ));
        OrderService service = new OrderService(resolver);

        assertThat(service.order("VIP", List.of(new OrderItem("책", 10_000, 2),
                new OrderItem("펜", 5_000, 1))))
                .isEqualTo(new OrderResult(25_000, 5_000, 20_000));
        assertThat(service.order("GOLD", List.of(new OrderItem("펜", 9_999, 1))))
                .isEqualTo(new OrderResult(9_999, 999, 9_000));
        assertThat(service.order("FIXED", List.of(new OrderItem("펜", 700, 1))))
                .isEqualTo(new OrderResult(700, 700, 0));
        assertThat(service.order("FIXED", List.of(new OrderItem("펜", 0, 1))))
                .isEqualTo(new OrderResult(0, 0, 0));
        assertThatThrownBy(() -> service.order("BASIC", List.of(new OrderItem("펜", 100, 1))))
                .isInstanceOf(UnknownGradeException.class);
        assertThatThrownBy(() -> service.order(null, List.of(new OrderItem("펜", 100, 1))))
                .isInstanceOf(UnknownGradeException.class);
    }
}
