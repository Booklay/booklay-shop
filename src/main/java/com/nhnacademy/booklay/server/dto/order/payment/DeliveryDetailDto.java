package com.nhnacademy.booklay.server.dto.order.payment;

import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class DeliveryDetailDto {

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

    public DeliveryDetailDto(DeliveryDetail deliveryDetail) {
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
