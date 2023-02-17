package com.nhnacademy.booklay.server.dto.order;

import com.nhnacademy.booklay.server.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class OrderListRetrieveResponse {
    private Long orderNo;
    private Long orderStatusCodeNo;
    @Setter
    private String orderStatusName;
    private Long productPriceSum;
    private Long paymentPrice;

    private String orderTitle;
    private Integer pointAccumulate;


    public OrderListRetrieveResponse(Order order) {
        this.orderNo = order.getId();
        this.orderStatusCodeNo = order.getOrderStatusCodeNo();
        this.productPriceSum = order.getProductPriceSum();
        this.paymentPrice = order.getPaymentPrice();
        this.orderTitle = order.getOrderTitle();
        this.pointAccumulate = order.getPointAccumulate();
    }
}
