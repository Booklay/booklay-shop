package com.nhnacademy.booklay.server.controller.admin.coupon;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.service.coupon.CouponAdminService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/admin/coupons")
public class CouponAdminController {

    private final CouponAdminService couponAdminService;

    @PostMapping
    public ResponseEntity<Object> createCoupon(@Valid @RequestBody CouponCreateRequest couponRequest) {
        couponAdminService.createCoupon(couponRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(null);
    }

    @GetMapping("/pages")
    public ResponseEntity<PageResponse<CouponRetrieveResponse>> retrieveAllCoupons(Pageable pageable) {
        Page<CouponRetrieveResponse> couponPage = couponAdminService.retrieveAllCoupons(pageable);

        PageResponse<CouponRetrieveResponse> couponPageResponse = new PageResponse<>(couponPage.getNumber(), couponPage.getSize(),
                couponPage.getTotalPages(), couponPage.getContent());

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(couponPageResponse);
    }

    @GetMapping("/{couponId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CouponDetailRetrieveResponse> retrieveCouponDetail(@PathVariable Long couponId) {
        CouponDetailRetrieveResponse couponDetailRetrieveResponse =
            couponAdminService.retrieveCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(couponDetailRetrieveResponse);
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
