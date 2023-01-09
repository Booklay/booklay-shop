package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponAdminServiceImpl implements CouponAdminService{

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public List<CouponRetrieveResponse> retrieveAllCoupons() {
        //List<CouponRetrieveResponse> couponList = couponRepository.getCouponsDto();

        return null;
    }

    @Transactional(readOnly = true)
    public CouponDetailRetrieveResponse retrieveCoupon(Long couponId) {
        //CouponDetailRetrieveDto coupon = couponRepository.getCouponById(couponId).orElseThrow(() -> new IllegalArgumentException("No Such Coupon."));

        return null;
    }

    @Transactional
    public void updateCoupon(Long couponId) {
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        couponRepository.deleteById(couponId);
    }
}
