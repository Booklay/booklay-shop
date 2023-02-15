package com.nhnacademy.booklay.server.dto.delivery.request;

import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.Member;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeliveryDestinationCURequest {
    @NotBlank
    private final String name;
    @NotBlank
    private final String zipCode;
    @NotBlank
    private final String address;
    private final String addressDetail;
    private final String addressSubDetail;
    @NotBlank
    private final String receiver;
    @NotBlank
    private final String receiverPhoneNo;
    @NotNull
    private final Boolean isDefaultDestination;

    public DeliveryDestination toEntity(Member member) {
        return DeliveryDestination.builder()
            .member(member)
            .name(this.name)
            .zipCode(this.zipCode)
            .address(this.address).addressDetail(this.addressDetail)
            .addressSubDetail(this.addressSubDetail)
            .receiver(this.receiver)
            .receiverPhoneNo(this.receiverPhoneNo)
            .isDefaultDestination(this.isDefaultDestination)
            .build();
    }
}
