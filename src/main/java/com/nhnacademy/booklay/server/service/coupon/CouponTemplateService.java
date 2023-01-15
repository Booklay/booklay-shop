package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateCreateRequest;
import com.nhnacademy.booklay.server.entity.CouponTemplate;
import javax.validation.Valid;

public interface CouponTemplateService {
    CouponTemplate createCouponTemplate(
        @Valid CouponTemplateCreateRequest couponTemplateCreateRequest);

    CouponTemplate retrieveCouponTemplate(Long couponTemplateNum);
}
