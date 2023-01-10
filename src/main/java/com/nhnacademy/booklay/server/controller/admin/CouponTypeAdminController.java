package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeRetrieveResponse;
import com.nhnacademy.booklay.server.service.coupon.CouponTypeService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/couponTypes")
public class CouponTypeAdminController {

    private final CouponTypeService couponTypeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CouponTypeRetrieveResponse> retrieveAllCouponTypes() {
        List<CouponTypeRetrieveResponse> couponTypeList = couponTypeService.retrieveAllCouponTypes();

        return couponTypeList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCouponType(@Valid @RequestBody CouponTypeCURequest couponTypeRequest) {
       couponTypeService.createCouponType(couponTypeRequest);
    }

    @PutMapping("/{couponTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCouponType(@PathVariable Long couponTypeId, @Valid @RequestBody CouponTypeCURequest couponTypeRequest) {
        couponTypeService.updateCouponType(couponTypeId, couponTypeRequest);
    }

    @DeleteMapping("/{couponTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCouponType(@PathVariable Long couponTypeId) {
        couponTypeService.deleteCouponType(couponTypeId);
    }

}
