package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "delivery_detail")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DeliveryDetail {

    @Id
    @Column(name = "delivery_detail_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code", nullable = false)
    private DeliveryStatusCode statusCode;

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
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder
    public DeliveryDetail(Order order, DeliveryStatusCode statusCode, String zipCode,
                          String address,
                          String sender, String senderPhoneNumber, String receiver,
                          String receiverPhoneNumber) {
        this.order = order;
        this.statusCode = statusCode;
        this.zipCode = zipCode;
        this.address = address;
        this.sender = sender;
        this.senderPhoneNumber = senderPhoneNumber;
        this.receiver = receiver;
        this.receiverPhoneNumber = receiverPhoneNumber;
    }
}
