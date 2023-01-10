package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponAdminServiceImpl implements CouponAdminService{

    private final CouponRepository couponRepository;

    public void createCoupon(CouponCURequest couponRequest) {

    }

    @Transactional(readOnly = true)
    public List<CouponRetrieveResponse> retrieveAllCoupons() {
        List<Coupon> couponList = couponRepository.findAll();

        return couponList.stream().map(c -> CouponRetrieveResponse.fromEntity(c)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CouponDetailRetrieveResponse retrieveCoupon(Long couponId) {
        return CouponDetailRetrieveResponse.fromEntity(couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("No Such Coupon.")));
    }

    public void updateCoupon(Long couponId, CouponCURequest couponRequest) {

    }

    public void deleteCoupon(Long couponId) {
        couponRepository.deleteById(couponId);
    }

}
