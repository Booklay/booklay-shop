package com.nhnacademy.booklay.server.dto.coupon;

import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CouponRetrieveResponse {
    private final Long id;
    private final String name;
    private final String couponType;
    private final int amount;
    private final int minimumUseAmount;
    private final int maximumDiscountAmount;
    private final boolean isLimited;

    @Builder
    public CouponRetrieveResponse(Long id, String name, CouponType couponType, int amount,
                                  int minimumUseAmount, int maximumDiscountAmount,
                                  boolean isLimited) {
        this.id = id;
        this.name = name;
        this.couponType = couponType.getName();
        this.amount = amount;
        this.minimumUseAmount = minimumUseAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.isLimited = isLimited;
    }

    public static CouponRetrieveResponse fromEntity(Coupon coupon) {
        return CouponRetrieveResponse.builder()
            .id(coupon.getId())
            .name(coupon.getName())
            .couponType(coupon.getCouponType())
            .amount(coupon.getAmount())
            .minimumUseAmount(coupon.getMinimumUseAmount())
            .minimumUseAmount(coupon.getMaximumDiscountAmount())
            .isLimited(coupon.getIsLimited())
            .build();
    }
}
