package com.nhnacademy.booklay.server.entity;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "delivery_destination")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("java:S107")
public class DeliveryDestination {

    @Id
    @Column(name = "delivery_destination_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private String name;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "address_sub_detail")
    private String addressSubDetail;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "receiver_phone_no")
    private String receiverPhoneNo;

    @Setter
    @Column(name = "is_default_destination")
    private Boolean isDefaultDestination;

    @Builder
    public DeliveryDestination(Member member, String name, String zipCode, String address,
                               String addressDetail, String addressSubDetail,
                               String receiver, String receiverPhoneNo,
                               Boolean isDefaultDestination) {
        this.member = member;
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.addressSubDetail = addressSubDetail;
        this.receiver = receiver;
        this.receiverPhoneNo = receiverPhoneNo;
        this.isDefaultDestination = isDefaultDestination;
    }

    public DeliveryDestination update(DeliveryDestinationCURequest requestDto) {
        this.name = requestDto.getName();
        this.zipCode = requestDto.getZipCode();
        this.address = requestDto.getAddress();
        this.addressDetail = requestDto.getAddressDetail();
        this.addressSubDetail = requestDto.getAddressSubDetail();
        this.receiver = requestDto.getReceiver();
        this.receiverPhoneNo = requestDto.getReceiverPhoneNo();
        this.isDefaultDestination = requestDto.getIsDefaultDestination();
        return this;
    }
}
