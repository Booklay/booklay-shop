package com.nhnacademy.booklay.server.controller.admin.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.service.coupon.CouponAdminService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 김승혜
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponAdminController {

    private final CouponAdminService couponAdminService;

    @GetMapping("/pages/{pageNum}")
    @ResponseStatus(HttpStatus.OK)
    public List<CouponRetrieveResponse> retrieveAllCoupons(@PathVariable int pageNum) {
        return couponAdminService.retrieveAllCoupons(pageNum);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCoupon(@Valid @RequestBody CouponCreateRequest couponRequest) {
        couponAdminService.createCoupon(couponRequest);
    }

    @GetMapping("/{couponId}")
    @ResponseStatus(HttpStatus.OK)
    public CouponDetailRetrieveResponse retrieveCouponDetail(@PathVariable Long couponId) {
        return couponAdminService.retrieveCoupon(couponId);
    }

    @PutMapping("/{couponId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCoupon(@PathVariable Long couponId, @RequestBody
    CouponUpdateRequest couponRequest) {
        couponAdminService.updateCoupon(couponId, couponRequest);
    }

    @DeleteMapping("/{couponId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCoupon(@PathVariable Long couponId) {
        couponAdminService.deleteCoupon(couponId);
    }
}
