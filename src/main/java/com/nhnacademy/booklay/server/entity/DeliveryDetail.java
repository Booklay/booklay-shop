package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "delivery_detail")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("java:S107")
public class DeliveryDetail {

    @Id
    @Column(name = "delivery_detail_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no", nullable = false, insertable = false, updatable = false)
    private Order order;
    @Column(name = "order_no")
    private Long orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code", nullable = false, insertable = false, updatable = false)
    private DeliveryStatusCode statusCode;
    @Column(name = "code")
    private Integer deliveryStatusCodeNo;

    @Column(name = "zip_code", length = 5, nullable = false)
    private String zipCode;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(length = 50, nullable = false)
    private String sender;

    @Column(name = "sender_phone_no", length = 15, nullable = false)
    private String senderPhoneNumber;

    @Column(length = 50, nullable = false)
    private String receiver;

    @Column(name = "receiver_phone_no", length = 15, nullable = false)
    private String receiverPhoneNumber;

    @Setter
    @Column(length = 50)
    private String memo;

    @Setter
    @Column(name = "invoice_no", length = 20)
    private String invoiceNo;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "delivery_started_at", nullable = false)
    private LocalDateTime deliveryStartAt;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "delivery_completed_at")
    private LocalDateTime completedAt;

    @Builder

    public DeliveryDetail(Order order, Long orderNo, DeliveryStatusCode statusCode, Integer deliveryStatusCodeNo,
                          String zipCode, String address, String sender, String senderPhoneNumber,
                          String receiver, String receiverPhoneNumber, String memo) {
        this.order = order;
        this.orderNo = orderNo;
        this.statusCode = statusCode;
        this.deliveryStatusCodeNo = deliveryStatusCodeNo;
        this.zipCode = zipCode;
        this.address = address;
        this.sender = sender;
        this.senderPhoneNumber = senderPhoneNumber;
        this.receiver = receiver;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.memo = memo;
    }
}
