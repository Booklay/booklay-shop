package com.nhnacademy.booklay.server.exception.order;

public class NotEnoughPointException extends CheckOrderException {
    public NotEnoughPointException() {
        super("보유 포인트가 부족합니다.");
    }
}
