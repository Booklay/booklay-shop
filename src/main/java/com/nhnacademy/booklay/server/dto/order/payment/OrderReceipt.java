package com.nhnacademy.booklay.server.dto.order.payment;

import com.nhnacademy.booklay.server.dto.order.OrderProductDto;
import com.nhnacademy.booklay.server.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class OrderReceipt {
    private Long orderNo;
    private Long orderStatusNo;
    @Setter
    private String orderStatus;
    private LocalDateTime orderedAt;
    private Long productPriceSum;
    private Long deliveryPrice;
    private Long discountPrice;
    private Long pointUsePrice;
    private Long paymentPrice;
    private Long paymentMethod;
    private Long giftWrappingPrice;

    @Setter
    List<DeliveryDetailDto> deliveryDetailDtoList;
    @Setter
    List<OrderProductDto> orderProductDtoList;

    public OrderReceipt(Order order) {
        this.orderNo            = order.getId();
        this.orderStatusNo      = order.getOrderStatusCodeNo();
        this.orderedAt          = order.getOrderedAt();
        this.productPriceSum    = order.getProductPriceSum();
        this.deliveryPrice      = order.getDeliveryPrice();
        this.discountPrice      = order.getDiscountPrice();
        this.pointUsePrice      = order.getPointUsePrice();
        this.paymentPrice       = order.getPaymentPrice();
        this.paymentMethod      = order.getPaymentMethod();
        this.giftWrappingPrice  = order.getGiftWrappingPrice();
    }
}
