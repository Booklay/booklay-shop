package com.nhnacademy.booklay.server.dto.cart;

import com.nhnacademy.booklay.server.service.cart.MemberCartService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartServiceAndKeyDto {
    private final MemberCartService cartService;
    private final String key;
}
