package com.nhnacademy.booklay.server.dto.cart;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    @NotNull
    private Long productNo;
    @NotNull
    private Integer count;
}
