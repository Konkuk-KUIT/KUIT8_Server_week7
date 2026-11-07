package order;

/** 상품 수량이 1개 미만일 때 사용하는 예외입니다. */
public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException() {
        super("상품 수량은 1개 이상이어야 합니다.");
    }
}
