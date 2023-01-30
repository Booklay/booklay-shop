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
    @NotNull
    private final Boolean isDefaultDestination;

    public DeliveryDestination toEntity(Member member) {
        return DeliveryDestination.builder()
                                  .member(member)
                                  .name(this.name)
                                  .zipCode(this.zipCode)
                                  .address(this.address)
                                  .isDefaultDestination(this.isDefaultDestination)
                                  .build();
    }
}
