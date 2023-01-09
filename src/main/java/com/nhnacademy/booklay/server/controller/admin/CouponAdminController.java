package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.service.coupon.CouponAdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CouponAdminController {

    private final CouponAdminServiceImpl couponAdminService;

    @GetMapping("/coupons")
    public String retrieveAllCoupons() {
        couponAdminService.retrieveAllCoupons();
        return "";
    }

    @GetMapping("/coupons/{couponId}")
    public String retrieveCoupon(@PathVariable Long couponId) {
        couponAdminService.retrieveCoupon(couponId);
        return "";
    }

    @PutMapping("/coupons/{couponId}")
    public String updateCoupon(@PathVariable Long couponId) {
        couponAdminService.updateCoupon(couponId);
        return "";
    }

    @DeleteMapping("/coupons/{couponId}")
    public void deleteCoupon(@PathVariable Long couponId) {
        couponAdminService.deleteCoupon(couponId);
    }
}
