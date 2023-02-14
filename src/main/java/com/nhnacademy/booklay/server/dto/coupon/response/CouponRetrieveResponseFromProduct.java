package com.nhnacademy.booklay.server.dto.coupon.response;

import lombok.Getter;

@Getter

public class CouponRetrieveResponseFromProduct {
    private Long id;
    private String name;
    private String typeName;
    private int amount;
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    private Boolean isLimited;
    private String couponCode;
    private Long categoryNo;
    private Long productNo;


}
