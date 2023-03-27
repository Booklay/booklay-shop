package com.nhnacademy.booklay.server.exception.order;

public class OrderSheetValidCheckFailException extends CheckOrderException {
    public OrderSheetValidCheckFailException() {
        super("유효하지 않은 주문입니다.");
    }
}
