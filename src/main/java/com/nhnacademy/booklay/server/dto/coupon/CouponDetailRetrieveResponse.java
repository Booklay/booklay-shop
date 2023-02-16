package com.nhnacademy.booklay.server.dto.coupon;

import com.nhnacademy.booklay.server.entity.Coupon;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@Builder
@ToString
public class CouponDetailRetrieveResponse {
    private final Long id;
    private final String name;
    private final String typeName;
    private final int amount;
    private final Long categoryId;
    private final Long productId;
    private final int minimumUseAmount;
    private final int maximumDiscountAmount;
    private final LocalDateTime issuanceDeadlineAt;
    private final Boolean isDuplicatable;
    private final Boolean isLimited;

    public static CouponDetailRetrieveResponse fromEntity(Coupon coupon) {
        return CouponDetailRetrieveResponse.builder()
                                           .id(coupon.getId())
                                           .name(coupon.getName())
                                           .typeName(coupon.getCouponType().getName())
                                           .amount(coupon.getAmount())
                                           .minimumUseAmount(coupon.getMinimumUseAmount())
                                           .minimumUseAmount(coupon.getMaximumDiscountAmount())
                                           .isDuplicatable(coupon.getIsDuplicatable())
                                           .isLimited(coupon.getIsLimited())
                                           .build();
    }
}
