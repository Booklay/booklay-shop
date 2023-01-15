package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.entity.CouponTemplate;

public interface CouponService {
    void createAndIssueCouponByTemplate(CouponTemplate couponTemplate, Long memberId);
}
