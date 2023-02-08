package com.nhnacademy.booklay.server.dto.coupon.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueResponseMessage {
    private String uuid;
    private String message;
}