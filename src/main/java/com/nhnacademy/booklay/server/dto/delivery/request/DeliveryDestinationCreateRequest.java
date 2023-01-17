package com.nhnacademy.booklay.server.dto.delivery.request;

import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.Member;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDestinationCreateRequest {
    @NotNull
    private Long memberNo;
    @NotBlank
    private String name;
    @NotBlank
    private String zipCode;
    @NotBlank
    private String address;
    @NotBlank
    private Boolean isDefaultDestination;

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
