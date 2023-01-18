package com.nhnacademy.booklay.server.dto.coupon;

import com.nhnacademy.booklay.server.entity.CouponTemplate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CouponTemplateRetrieveResponse {
    private final Long id;
    private final String name;
    private final Boolean isOrderCoupon;
    private final Long applyItemId;
    private final int amount;
    private final int minimumUseAmount;
    private final int maximumDiscountAmount;

    private final Boolean isDuplicatable;

    @Builder
    public CouponTemplateRetrieveResponse(Long id, String name, Boolean isOrderCoupon,
                                          Long applyItemId,
                                          int amount, int minimumUseAmount,
                                          int maximumDiscountAmount,
                                          Boolean isDuplicatable) {
        this.id = id;
        this.name = name;
        this.isOrderCoupon = isOrderCoupon;
        this.applyItemId = applyItemId;
        this.amount = amount;
        this.minimumUseAmount = minimumUseAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.isDuplicatable = isDuplicatable;
    }

    @Builder


    public static CouponTemplateRetrieveResponse fromEntity(CouponTemplate couponTemplate) {
        return CouponTemplateRetrieveResponse.builder()
            .id(couponTemplate.getId())
            .name(couponTemplate.getName())
            .isOrderCoupon(couponTemplate.getIsOrderCoupon())
            .applyItemId(couponTemplate.getApplyItemId())
            .amount(couponTemplate.getAmount())
            .minimumUseAmount(couponTemplate.getMinimumUseAmount())
            .minimumUseAmount(couponTemplate.getMaximumDiscountAmount())
            .isDuplicatable(couponTemplate.getIsDuplicatable())
            .build();
    }
}
