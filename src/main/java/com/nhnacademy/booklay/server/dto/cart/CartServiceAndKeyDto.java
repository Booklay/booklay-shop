package com.nhnacademy.booklay.server.dto.cart;

import com.nhnacademy.booklay.server.service.cart.CartService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartServiceAndKeyDto {
    private final CartService cartService;
    private final String key;
}
