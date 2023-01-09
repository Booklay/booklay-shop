package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.dto.coupon.CouponCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.service.coupon.CouponAdminService;
import com.nhnacademy.booklay.server.service.coupon.CouponAdminServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CouponAdminController {

    private final CouponAdminService couponAdminService;

    @GetMapping("/coupons/pages/{pageNum}")
    public List<CouponRetrieveResponse> retrieveAllCoupons(@PathVariable int pageNum) {
        return couponAdminService.retrieveAllCoupons();
    }

    @PostMapping("/coupons")
    public void createCoupon(@RequestBody CouponCURequest couponRequest) {
        couponAdminService.createCoupon(couponRequest);
    }

    @GetMapping("/coupons/{couponId}")
    public CouponDetailRetrieveResponse retrieveCouponDetail(@PathVariable Long couponId) {
        return couponAdminService.retrieveCoupon(couponId);
    }

    @PutMapping("/coupons/{couponId}")
    public void updateCoupon(@PathVariable Long couponId, @RequestBody CouponCURequest couponRequest) {
        couponAdminService.updateCoupon(couponId, couponRequest);
    }

    @DeleteMapping("/coupons/{couponId}")
    public void deleteCoupon(@PathVariable Long couponId) {
        couponAdminService.deleteCoupon(couponId);
    }
}
