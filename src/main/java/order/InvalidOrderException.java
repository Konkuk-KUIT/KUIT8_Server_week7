package order;

/** 주문 목록이 비었거나 주문할 수 없는 상품이 포함된 경우 사용하는 예외입니다. */
public class InvalidOrderException extends IllegalArgumentException {
    public InvalidOrderException() {
        super("주문에는 상품이 하나 이상 필요합니다.");
    }
}
