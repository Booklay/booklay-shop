package com.nhnacademy.booklay.server.exception.order;

public class CouponValidatingFailException extends CheckOrderException {
    public CouponValidatingFailException() {
        super("유효하지 않은 쿠폰이 있습니다.");
    }
}
