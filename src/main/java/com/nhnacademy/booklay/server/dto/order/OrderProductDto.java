package com.nhnacademy.booklay.server.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductDto {
    private Long productNo;
    private Integer count;
    private Long price;
}
