package com.nhnacademy.booklay.server.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartAddRequest implements Serializable {
    @NotNull
    private String cartId;
    @NotNull
    private Long productNo;
    @NotNull
    private Integer count;
}
