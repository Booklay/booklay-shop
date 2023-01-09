package com.nhnacademy.booklay.server.dto.coupon;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponCURequest {
    private String name;
    private Long memberId;
    private String typeCode;
    private int amount;
    private long categoryId;
    private long productId;
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    private LocalDateTime issuanceDeadlineAt;
    private Boolean isDuplicatable;
}
