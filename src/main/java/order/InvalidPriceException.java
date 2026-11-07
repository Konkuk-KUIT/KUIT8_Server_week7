package order;

/** 상품 단가가 0원 미만일 때 사용하는 예외입니다. */
public class InvalidPriceException extends IllegalArgumentException {
    public InvalidPriceException() {
        super("상품 단가는 0원 이상이어야 합니다.");
    }
}
