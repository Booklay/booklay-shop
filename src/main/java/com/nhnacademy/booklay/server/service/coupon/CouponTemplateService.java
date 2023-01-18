package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateUpdateRequest;
import com.nhnacademy.booklay.server.entity.CouponTemplate;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponTemplateService {
    CouponTemplate createCouponTemplate(
        @Valid CouponTemplateCreateRequest couponTemplateCreateRequest);

    CouponTemplate retrieveCouponTemplate(Long couponTemplateNum);

    Page<CouponTemplateRetrieveResponse> retrieveAllCouponTemplate(Pageable pageable);

    CouponTemplateDetailRetrieveResponse retrieveCouponTemplateDetailResponse(Long couponTemplateId);

    CouponTemplate updateCouponTemplate(
        CouponTemplateUpdateRequest couponTemplateUpdateRequest);

    void deleteCouponTemplate(Long couponTemplateId);
}
