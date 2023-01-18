package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.exception.category.NotFoundException;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author 오준후
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{
    private final CouponRepository couponRepository;

    @Override
    public Coupon retrieveCoupon(Long couponId){
        return couponRepository.findById(couponId)
            .orElseThrow(() -> new NotFoundException(Coupon.class.toString(), couponId));
    }
}
