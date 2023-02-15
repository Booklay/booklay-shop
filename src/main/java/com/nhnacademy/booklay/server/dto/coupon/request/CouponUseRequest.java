package com.nhnacademy.booklay.server.dto.coupon.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CouponUseRequest {

    List<CouponUsingDto> productCouponList = new ArrayList<>();
    List<CouponUsingDto> categoryCouponList = new ArrayList<>();
}
