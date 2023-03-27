package com.nhnacademy.booklay.server.exception.order;

public class OrderSheetAlreadyProcessedException extends SaveOrderException {
    public OrderSheetAlreadyProcessedException() {
        super("이미 처리된 주문입니다.");
    }
}
