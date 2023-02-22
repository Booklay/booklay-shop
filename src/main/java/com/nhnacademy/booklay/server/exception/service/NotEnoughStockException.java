package com.nhnacademy.booklay.server.exception.service;

import com.nhnacademy.booklay.server.dto.cart.CartDto;

public class NotEnoughStockException extends RuntimeException {
    public <T> NotEnoughStockException(CartDto cartDto) {
        super("******** Not Enough Stock ********\n"
                  + "ProductNo  : " + cartDto.getProductNo() + "\n"
                  + "Count      : " + cartDto.getCount());
    }
}
