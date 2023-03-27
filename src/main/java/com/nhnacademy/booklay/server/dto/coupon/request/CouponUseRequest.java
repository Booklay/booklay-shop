package com.nhnacademy.booklay.server.dto.coupon.request;

import com.nhnacademy.booklay.server.dto.coupon.response.CouponRetrieveResponseFromProduct;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CouponUseRequest {

    List<CouponUsingDto> productCouponList = new ArrayList<>();
    List<CouponUsingDto> categoryCouponList = new ArrayList<>();

    public void setCouponData(CouponRetrieveResponseFromProduct couponData) {
        if (couponData.getProductNo()!=null){
            //상품쿠폰변환
            productCouponList.add(new CouponUsingDto(
                couponData.getCouponCode(),
                couponData.getId(),
                couponData.getProductNo()));
        }else {
            //주문쿠폰변환
            categoryCouponList.add(new CouponUsingDto(
                couponData.getCouponCode(),
                couponData.getId(),
                couponData.getCategoryNo()));
        }
    }
}
