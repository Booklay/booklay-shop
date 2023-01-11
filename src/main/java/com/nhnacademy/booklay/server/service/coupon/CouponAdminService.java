package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponAdminService {

    void createCoupon(CouponCreateRequest couponRequest);
    Page<Coupon> retrieveAllCoupons(Pageable pageable);
    CouponDetailRetrieveResponse retrieveCoupon(Long couponId);
    void updateCoupon(Long couponId, CouponUpdateRequest couponRequest);

    void deleteCoupon(Long couponId);

}
