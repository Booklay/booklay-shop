package com.nhnacademy.booklay.server.dto.order;

import com.nhnacademy.booklay.server.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductDto {
    private Long productNo;
    private Integer count;
    private Long price;

    public OrderProductDto(OrderProduct orderProduct) {
        this.productNo = orderProduct.getProductNo();
        this.count = orderProduct.getCount();
        this.price = (long) orderProduct.getPrice();
    }
}
