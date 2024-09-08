package org.jyj.order.exception.Item;

public class NotAllocateStockException extends RuntimeException {
    private static final String MESSAGE = "해당 재고는 현재 다른 작업에 의해 사용 중입니다. 잠시 후 다시 시도해 주세요.";

    public NotAllocateStockException() {
        super(MESSAGE);
    }
}
