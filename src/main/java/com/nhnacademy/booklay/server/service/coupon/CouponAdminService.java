package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.repository.CouponRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponAdminService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public void retrieveAllCoupons() {
        List<Coupon> couponList = couponRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void retrieveCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("No Such Coupon."));
    }

    @Transactional
    public void updateCoupon(Long couponId) {
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        couponRepository.deleteById(couponId);
    }
}
