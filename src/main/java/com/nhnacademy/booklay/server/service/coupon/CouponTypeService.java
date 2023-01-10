package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeRetrieveResponse;
import java.util.List;

public interface CouponTypeService {
    List<CouponTypeRetrieveResponse> retrieveAllCouponTypes();

    void createCouponType(CouponTypeCURequest couponTypeRequest);

    void deleteCouponType(Long couponTypeId);

    void updateCouponType();
}
