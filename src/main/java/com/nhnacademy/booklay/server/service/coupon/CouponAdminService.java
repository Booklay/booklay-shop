package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import java.util.List;

public interface CouponAdminService {

    void createCoupon(CouponCURequest couponRequest);
    List<CouponRetrieveResponse> retrieveAllCoupons();
    CouponDetailRetrieveResponse retrieveCoupon(Long couponId);
    void updateCoupon(Long couponId, CouponCURequest couponRequest);

    void deleteCoupon(Long couponId);

}
