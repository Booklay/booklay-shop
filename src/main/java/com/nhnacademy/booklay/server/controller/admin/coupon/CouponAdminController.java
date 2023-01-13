package com.nhnacademy.booklay.server.controller.admin.coupon;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponIssueRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.service.coupon.CouponAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CouponCreateRequest couponRequest) {
        couponAdminService.createCoupon(couponRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/pages")
    public ResponseEntity<PageResponse<CouponRetrieveResponse>> retrieveAllCoupons(@PageableDefault Pageable pageable) {
        Page<CouponRetrieveResponse> couponPage = couponAdminService.retrieveAllCoupons(pageable);

        PageResponse<CouponRetrieveResponse> couponPageResponse = new PageResponse<>(couponPage.getNumber(), couponPage.getSize(),
                couponPage.getTotalPages(), couponPage.getContent());

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(couponPageResponse);
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponDetailRetrieveResponse> retrieveCouponDetail(@PathVariable Long couponId) {
        CouponDetailRetrieveResponse couponDetailRetrieveResponse =
            couponAdminService.retrieveCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(couponDetailRetrieveResponse);
    }

    @PutMapping("/{couponId}")
    public ResponseEntity<Void> updateCoupon(@PathVariable Long couponId, @Valid @RequestBody CouponUpdateRequest couponRequest) {
        couponAdminService.updateCoupon(couponId, couponRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long couponId) {
        couponAdminService.deleteCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/issue")
    public ResponseEntity<Void> issueCouponToMember(@Valid @RequestBody
                                                    CouponIssueRequest couponRequest) {
        couponAdminService.issueCoupon(couponRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
