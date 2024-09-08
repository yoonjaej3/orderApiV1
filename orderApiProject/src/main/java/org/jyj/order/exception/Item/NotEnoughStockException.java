package org.jyj.order.exception.Item;

public class NotEnoughStockException extends RuntimeException {
    private static final String MESSAGE = "재고가 부족합니다.";

    public NotEnoughStockException() {
        super(MESSAGE);
    }
}
