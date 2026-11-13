package order;

    /** 주문의 상품 한 종류를 나타냅니다. 단가는 원 단위이며 수량은 개수입니다. */
    public record OrderItem(String name, int unitPrice, int quantity) {

    }
