package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.dto.coupon.CouponCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.service.coupon.CouponAdminService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponAdminController {

    private final CouponAdminService couponAdminService;

    @GetMapping("/coupons/pages/{pageNum}")
    public List<CouponRetrieveResponse> retrieveAllCoupons(@PathVariable int pageNum) {
        return couponAdminService.retrieveAllCoupons(pageNum);
    }

    @PostMapping("/coupons")
    public void createCoupon(@Valid @RequestBody CouponCURequest couponRequest) {
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
