package com.nhnacademy.booklay.server.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartRetrieveResponse {
    private Long productNo;
    private String productName;
    private Long productPrice;
    private Integer productCount;
}
