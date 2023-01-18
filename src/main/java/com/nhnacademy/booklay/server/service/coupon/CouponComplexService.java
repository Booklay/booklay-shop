package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.entity.CouponTemplate;

public interface CouponComplexService {
    void createAndIssueCouponByTemplate(CouponTemplate couponTemplate, Long memberId);
}
