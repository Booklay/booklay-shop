package com.nhnacademy.booklay.server.controller.admin.coupon;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeRetrieveResponse;
import com.nhnacademy.booklay.server.service.coupon.CouponTypeService;
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
@RequestMapping("/admin/couponTypes")
public class CouponTypeAdminController {

    private final CouponTypeService couponTypeService;

    @GetMapping
    public ResponseEntity<PageResponse<CouponTypeRetrieveResponse>> retrieveAllCouponTypes(@PageableDefault
                                                                                           Pageable pageable) {
        Page<CouponTypeRetrieveResponse> couponTypePage = couponTypeService.retrieveAllCouponTypes(pageable);

        PageResponse<CouponTypeRetrieveResponse> couponTypePageResponse = new PageResponse<>(couponTypePage.getNumber(), couponTypePage.getSize(), couponTypePage.getTotalPages(), couponTypePage.getContent());

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(couponTypePageResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createCouponType(@Valid @RequestBody CouponTypeCURequest couponTypeRequest) {
       couponTypeService.createCouponType(couponTypeRequest);

       return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{couponTypeId}")
    public ResponseEntity<Void> updateCouponType(@PathVariable Long couponTypeId, @Valid @RequestBody CouponTypeCURequest couponTypeRequest) {
        couponTypeService.updateCouponType(couponTypeId, couponTypeRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{couponTypeId}")
    public ResponseEntity<Void> deleteCouponType(@PathVariable Long couponTypeId) {
        couponTypeService.deleteCouponType(couponTypeId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
