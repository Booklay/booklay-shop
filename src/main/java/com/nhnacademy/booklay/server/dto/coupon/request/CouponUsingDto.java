package com.nhnacademy.booklay.server.dto.coupon.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponUsingDto {
    private String couponCode;
    private Long specifiedCouponNo;
    private Long usedTargetNo;

}
