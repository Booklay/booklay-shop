package com.nhnacademy.booklay.server.exception.order;

public class OrderSheetNotFoundException extends SaveOrderException {
    public OrderSheetNotFoundException() {
        super("확인할 수 없는 주문입니다.");
    }
}
