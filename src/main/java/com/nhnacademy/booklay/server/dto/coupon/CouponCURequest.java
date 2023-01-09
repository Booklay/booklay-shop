package com.nhnacademy.booklay.server.dto.coupon;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponCURequest {
    private final String name;
    private final Long memberId;
    private final String typeCode;
    private final int amount;
    private final long categoryId;
    private final long productId;
    private final int minimumUseAmount;
    private final int maximumDiscountAmount;
    private final LocalDateTime issuanceDeadlineAt;
    private final Boolean isDuplicatable;
}
