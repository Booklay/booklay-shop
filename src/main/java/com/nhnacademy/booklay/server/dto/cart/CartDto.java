package com.nhnacademy.booklay.server.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    @NotNull
    private Long productNo;
    @NotNull
    private Integer count;
}
