package com.nhnacademy.booklay.server.dto.coupon;

import com.nhnacademy.booklay.server.entity.CouponTemplate;
import javax.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CouponTemplateDetailRetrieveResponse {
    private final Long id;
    private final Long imageNo;
    private final Long typeCode;
    private final String name;
    private final Boolean isOrderCoupon;
    private final Long applyItemId;
    private final int amount;
    private final int minimumUseAmount;
    private final int maximumDiscountAmount;
    private final Integer validateTerm;
    private final Boolean isDuplicatable;

    @Builder
    public CouponTemplateDetailRetrieveResponse(Long id, Long imageNo, Long typeCode, String name,
                                                Boolean isOrderCoupon, Long applyItemId, int amount,
                                                int minimumUseAmount, int maximumDiscountAmount,
                                                Integer validateTerm, Boolean isDuplicatable) {
        this.id = id;
        this.imageNo = imageNo;
        this.typeCode = typeCode;
        this.name = name;
        this.isOrderCoupon = isOrderCoupon;
        this.applyItemId = applyItemId;
        this.amount = amount;
        this.minimumUseAmount = minimumUseAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.validateTerm = validateTerm;
        this.isDuplicatable = isDuplicatable;
    }



    public static CouponTemplateDetailRetrieveResponse fromEntity(CouponTemplate couponTemplate) {
        return CouponTemplateDetailRetrieveResponse.builder()
            .id(couponTemplate.getId())
            .imageNo(couponTemplate.getImageNo())
            .typeCode(couponTemplate.getTypeCode())
            .name(couponTemplate.getName())
            .isOrderCoupon(couponTemplate.getIsOrderCoupon())
            .applyItemId(couponTemplate.getApplyItemId())
            .amount(couponTemplate.getAmount())
            .minimumUseAmount(couponTemplate.getMinimumUseAmount())
            .minimumUseAmount(couponTemplate.getMaximumDiscountAmount())
            .isDuplicatable(couponTemplate.getIsDuplicatable())
            .validateTerm(couponTemplate.getValidateTerm())
            .build();
    }
}
