package com.nhnacademy.booklay.server.dto.coupon;

import com.nhnacademy.booklay.server.entity.CouponType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponTypeRetrieveResponse {

    private final Long code;

    private final String name;

    public static CouponTypeRetrieveResponse fromEntity(CouponType couponType) {
        return new CouponTypeRetrieveResponse(couponType.getId(), couponType.getName());
    }
}
