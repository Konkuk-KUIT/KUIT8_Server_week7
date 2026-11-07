package order;

/** 등록되지 않은 회원 등급으로 주문할 때 사용하는 예외입니다. */
public class UnknownGradeException extends IllegalArgumentException {
    public UnknownGradeException(String grade) {
        super("등록되지 않은 등급입니다: " + grade);
    }
}
