package com.nhnacademy.booklay.server.exception.order;

public class ProductNotFoundException extends CheckOrderException {
    public ProductNotFoundException() {
        super("찾을 수 없는 제품이 포함되어 있습니다.");
    }
}
