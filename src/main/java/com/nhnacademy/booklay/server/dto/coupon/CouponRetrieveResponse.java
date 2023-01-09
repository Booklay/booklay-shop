package com.nhnacademy.booklay.server.dto.coupon;

import com.nhnacademy.booklay.server.entity.Coupon;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class CouponRetrieveResponse {
    private final Long id;
    private final String name;
    private final String couponType;
    private final int amount;
    private final int minimumUseAmount;
    private final int maximumDiscountAmount;

    public CouponRetrieveResponse fromEntity(Coupon coupon) {
        return CouponRetrieveResponse.builder()
            .id(coupon.getId())
            .name(coupon.getName())
            .couponType(coupon.getCouponType().getName())
            .amount(coupon.getAmount())
            .minimumUseAmount(coupon.getMinimumUseAmount())
            .minimumUseAmount(coupon.getMaximumDiscountAmount())
            .build();
    }
}
