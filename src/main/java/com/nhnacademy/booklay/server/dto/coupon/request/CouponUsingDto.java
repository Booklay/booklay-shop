package com.nhnacademy.booklay.server.dto.coupon.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponUsingDto {
    private String couponCode;
    private Long specifiedCouponNo;
    @Setter
    private Long usedTargetNo;

}
