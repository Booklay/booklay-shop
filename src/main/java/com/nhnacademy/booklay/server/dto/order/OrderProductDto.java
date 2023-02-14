package com.nhnacademy.booklay.server.dto.order;

import com.nhnacademy.booklay.server.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDto {
    private Long productNo;
    private String productName;
    private Integer count;
    private Integer price;

    public OrderProductDto(OrderProduct orderProduct) {
        this.productNo = orderProduct.getProductNo();
        this.count = orderProduct.getCount();
        this.price = orderProduct.getPrice();
        this.productName = orderProduct.getProduct().getTitle();
    }
}
