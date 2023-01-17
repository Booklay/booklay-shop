package com.nhnacademy.booklay.server.dto.delivery.response;

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
    private Long memberNo;
    private String name;
    private String zipCode;
    private String address;
    private Boolean isDefaultDestination;

    /**
     * dto projection을 위한 생성자
     *
     * @author 양승아
     */
    public DeliveryDestinationRetrieveResponse(Member member, String name, String zipCode,
                                               String address, Boolean isDefaultDestination) {
        this.memberNo = member.getMemberNo();
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.isDefaultDestination = isDefaultDestination;
    }
}
