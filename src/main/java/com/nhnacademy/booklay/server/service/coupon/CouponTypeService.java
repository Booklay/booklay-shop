package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeRetrieveResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponTypeService {

    void createCouponType(CouponTypeCURequest couponTypeRequest);

    Page<CouponTypeRetrieveResponse> retrieveAllCouponTypes(Pageable pageable);

    void updateCouponType(Long couponTypeId, CouponTypeCURequest couponTypeRequest);

    void deleteCouponType(Long couponTypeId);
}
