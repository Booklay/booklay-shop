package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import java.util.List;

public interface CouponAdminService {

    void createCoupon(CouponCreateRequest couponRequest);
    List<CouponRetrieveResponse> retrieveAllCoupons(int pageNum);
    CouponDetailRetrieveResponse retrieveCoupon(Long couponId);
    void updateCoupon(Long couponId, CouponUpdateRequest couponRequest);

    void deleteCoupon(Long couponId);

}
