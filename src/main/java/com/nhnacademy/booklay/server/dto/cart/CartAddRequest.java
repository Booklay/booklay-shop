package com.nhnacademy.booklay.server.dto.cart;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartAddRequest implements Serializable {
    @NotNull
    @Setter
    private String cartId;
    @NotNull
    private Long productNo;
    @NotNull
    private Integer count;
}
