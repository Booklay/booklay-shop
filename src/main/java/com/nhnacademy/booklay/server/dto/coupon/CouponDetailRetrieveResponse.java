package com.nhnacademy.booklay.server.dto.coupon;

import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class CouponDetailRetrieveResponse {
    private final Long id;
    private final Member member;
    private final String name;
    private final String typeName;
    private final int amount;
    private final Category category;
    private final Product product;
    private final int minimumUseAmount;
    private final int maximumDiscountAmount;
    private final LocalDateTime issuanceDeadlineAt;
    private final Boolean isDuplicatable;

    public static CouponDetailRetrieveResponse fromEntity(Coupon coupon) {
        return CouponDetailRetrieveResponse.builder()
            .id(coupon.getId())
            .name(coupon.getName())
            .typeName(coupon.getCouponType().getName())
            .amount(coupon.getAmount())
            .minimumUseAmount(coupon.getMinimumUseAmount())
            .minimumUseAmount(coupon.getMaximumDiscountAmount())
            .build();
    }
}
