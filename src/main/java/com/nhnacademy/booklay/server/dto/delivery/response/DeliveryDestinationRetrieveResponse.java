package com.nhnacademy.booklay.server.dto.delivery.response;

import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDestinationRetrieveResponse {
    private Long id;
    private Long memberNo;
    private String name;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String addressSubDetail;
    private String receiver;
    private String receiverPhoneNo;
    private Boolean isDefaultDestination;

    public static DeliveryDestinationRetrieveResponse fromEntity(
        DeliveryDestination deliveryDestination) {
        return DeliveryDestinationRetrieveResponse.builder()
            .id(deliveryDestination.getId())
            .memberNo(deliveryDestination.getMember().getMemberNo())
            .name(deliveryDestination.getName())
            .zipCode(deliveryDestination.getZipCode())
            .address(deliveryDestination.getAddress())
            .addressDetail(deliveryDestination.getAddressDetail())
            .addressSubDetail(deliveryDestination.getAddressSubDetail())
            .receiver(deliveryDestination.getReceiver())
            .receiverPhoneNo(deliveryDestination.getReceiverPhoneNo())
            .isDefaultDestination(deliveryDestination.getIsDefaultDestination())
            .build();
    }

    public DeliveryDestination toEntity(Member member) {
        return DeliveryDestination.builder()
            .name(this.name)
            .member(member)
            .zipCode(this.zipCode)
            .address(this.address)
            .addressDetail(this.addressDetail)
            .addressSubDetail(this.addressSubDetail)
            .receiver(this.receiver)
            .receiverPhoneNo(this.receiverPhoneNo)
            .isDefaultDestination(this.isDefaultDestination)
            .build();
    }
}
