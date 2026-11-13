package order;

import java.util.List;

// /** 리팩터링 전 코드입니다. 여러 책임이 한 메서드에 섞인 점을 비교해보세요. */
// public class LegacyOrderService {

//     public int order(String grade, List<OrderItem> items) {
//         int total = 0;
//         for (OrderItem item : items) {
//             total += item.xunitPrice() * item.quantity();
//         }

//         int discount = 0;
//         if (grade.equals("VIP")) {
//             discount = total * 20 / 100;
//         } else if (grade.equals("GOLD")) {
//             discount = total * 10 / 100;
//         }

//         int finalPrice = total - discount;
//         System.out.println("주문 완료");
//         System.out.println("결제 금액: " + finalPrice);
//         return finalPrice;
//     }
// }
