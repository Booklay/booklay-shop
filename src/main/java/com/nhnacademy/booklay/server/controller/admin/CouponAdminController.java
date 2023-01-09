package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.service.coupon.CouponAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponAdminController {

    private final CouponAdminService couponAdminService;

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
        //TODO: @RequestBody
        couponAdminService.updateCoupon(couponId);
        return "";
    }

    @DeleteMapping("/coupons/{couponId}")
    public void deleteCoupon(@PathVariable Long couponId) {
        couponAdminService.deleteCoupon(couponId);
    }
}
