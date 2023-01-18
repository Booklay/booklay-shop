package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeRetrieveResponse;
import com.nhnacademy.booklay.server.entity.CouponType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponTypeService {

    void createCouponType(CouponTypeCURequest couponTypeRequest);

    CouponType retrieveCouponType(Long couponTypeId);

    Page<CouponTypeRetrieveResponse> retrieveAllCouponTypes(Pageable pageable);
    void updateCouponType(Long couponTypeId, CouponTypeCURequest couponTypeRequest);
    void deleteCouponType(Long couponTypeId);
}
