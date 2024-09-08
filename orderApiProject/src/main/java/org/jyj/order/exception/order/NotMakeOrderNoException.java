package org.jyj.order.exception.order;

public class NotMakeOrderNoException extends RuntimeException {
    private static final String MESSAGE = "주문 번호 생성 작업이 현재 다른 작업에 의해 사용 중입니다. 잠시 후 다시 시도해 주세요.";

    public NotMakeOrderNoException() {
        super(MESSAGE);
    }
}
