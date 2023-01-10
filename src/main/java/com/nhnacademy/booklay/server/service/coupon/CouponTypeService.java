package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeRetrieveResponse;
import java.util.List;

public interface CouponTypeService {

    void createCouponType(CouponTypeCURequest couponTypeRequest);
    List<CouponTypeRetrieveResponse> retrieveAllCouponTypes();
    void updateCouponType(Long couponTypeId, CouponTypeCURequest couponTypeRequest);
    void deleteCouponType(Long couponTypeId);
}
