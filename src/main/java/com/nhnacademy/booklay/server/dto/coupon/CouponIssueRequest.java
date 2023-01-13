package com.nhnacademy.booklay.server.dto.coupon;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueRequest {

    @NotNull
    Long couponId;

    @NotNull
    Long memberId;
}