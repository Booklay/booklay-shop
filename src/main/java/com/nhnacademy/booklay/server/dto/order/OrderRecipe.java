package com.nhnacademy.booklay.server.dto.order;

import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.service.order.OrderStatusServiceImpl;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class OrderRecipe {
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


    private Integer deliveryStatusNo;
    @Setter
    private String deliveryStatus;
    private String zipCode;
    private String address;
    private String sender;
    private String senderPhoneNumber;
    private String receiver;
    private String receiverPhoneNumber;
    private String memo;
    private String invoiceNo;
    private LocalDateTime deliveryStartAt;
    private LocalDateTime completedAt;


    @Builder
    public OrderRecipe(DeliveryDetail deliveryDetail) {
        this.orderNo            = deliveryDetail.getOrder().getId();
        this.orderStatusNo      = deliveryDetail.getOrder().getOrderStatusCodeNo();
        this.orderedAt          = deliveryDetail.getOrder().getOrderedAt();
        this.productPriceSum    = deliveryDetail.getOrder().getProductPriceSum();
        this.deliveryPrice      = deliveryDetail.getOrder().getDeliveryPrice();
        this.discountPrice      = deliveryDetail.getOrder().getDiscountPrice();
        this.pointUsePrice      = deliveryDetail.getOrder().getPointUsePrice();
        this.paymentPrice       = deliveryDetail.getOrder().getPaymentPrice();
        this.paymentMethod      = deliveryDetail.getOrder().getPaymentMethod();
        this.giftWrappingPrice  = deliveryDetail.getOrder().getGiftWrappingPrice();

        this.deliveryStatusNo   = deliveryDetail.getDeliveryStatusCodeNo();
        this.zipCode            = deliveryDetail.getZipCode();
        this.address            = deliveryDetail.getAddress();
        this.sender             = deliveryDetail.getSender();
        this.senderPhoneNumber  = deliveryDetail.getSenderPhoneNumber();
        this.receiver           = deliveryDetail.getReceiver();
        this.receiverPhoneNumber= deliveryDetail.getReceiverPhoneNumber();
        this.memo               = deliveryDetail.getMemo();
        this.invoiceNo          = deliveryDetail.getInvoiceNo();
        this.deliveryStartAt    = deliveryDetail.getDeliveryStartAt();
        this.completedAt        = deliveryDetail.getCompletedAt();
    }
}
