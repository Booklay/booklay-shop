package com.nhnacademy.booklay.server.dto.coupon.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 쿠폰존에서 쿠폰을 발급 받기 위해, 쿠폰 서버로 전해지는 객체입니다.
 * 사용자는 shop 서버에서 검사합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueRequestMessage {
    private Long couponId;
    private Long memberId;
    private String uuid;
}
