package com.nhnacademy.booklay.server.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long productNo;
    private Integer count;
}
