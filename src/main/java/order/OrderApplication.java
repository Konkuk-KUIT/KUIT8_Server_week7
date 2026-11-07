package order;

import java.util.List;
import java.util.Map;

/** 정책 객체를 등록하고 주문 서비스를 실행하는 시작점입니다. */
public class OrderApplication {
    public static void main(String[] args) {
        // 실제 실행에서 사용할 정책을 등급별로 등록합니다.
        DiscountPolicyResolver resolver = new MapDiscountPolicyResolver(Map.of(
                "VIP", new VipDiscountPolicy(),
                "GOLD", new GoldDiscountPolicy(),
                "FIXED", new FixedDiscountPolicy()
        ));
        OrderService service = new OrderService(resolver);

        // 서비스가 결과를 반환하면, 출력은 이 시작점에서 담당합니다.
        OrderResult result = service.order("VIP", List.of(
                new OrderItem("책", 10_000, 2),
                new OrderItem("펜", 5_000, 1)
        ));
        System.out.println("주문 완료");
        System.out.println("주문 금액: " + result.orderAmount() + "원");
        System.out.println("할인 금액: " + result.discountAmount() + "원");
        System.out.println("최종 결제 금액: " + result.finalPrice() + "원");
    }
}
