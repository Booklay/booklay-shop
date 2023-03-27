package com.nhnacademy.booklay.server.dto.order.payment;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponUseRequest;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponRetrieveResponseFromProduct;
import com.nhnacademy.booklay.server.entity.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
public class OrderCheckDto {
    private OrderSheet orderSheet;
    private List<Long> productNoList = new ArrayList<>();
    private List<Product> productList;
    private Map<Long, CartDto> cartDtoMap = new HashMap<>(); //구매 상품 맵
    private Map<Long, Long> productPriceMap = new HashMap<>();
    private MultiValueMap<Long, CouponRetrieveResponseFromProduct> couponMap =
        new LinkedMultiValueMap<>();
    private MultiValueMap<String, Long> productCategoryMap = new LinkedMultiValueMap<>();
    private MultiValueMap<Long, Long> categoryListMap;
    private CouponUseRequest couponUseRequest = new CouponUseRequest();
    private Long pointAccumulateSum = 0L;// 적립 예상 포인트
    private Long productDiscountedTotalPrice = 0L; // 상품쿠폰이 적용된 상품가격 합계
    private Long productTotalPrice = 0L; // 상품 가격합계
    private Long paymentPrice = 0L; // 결제 금액
    private Long discountedTotalPrice = 0L; // 할인된 총 금액
    private Long orderCouponDiscountSum = 0L;
    public void setCouponList(List<CouponRetrieveResponseFromProduct> couponList){
        for(CouponRetrieveResponseFromProduct couponData : couponList){
            couponMap.add(couponData.getProductNo(), couponData);
            couponUseRequest.setCouponData(couponData);
        }
    }
    public void setOrderSheet(OrderSheet orderSheet) {
        this.orderSheet = orderSheet;
        for (CartDto cartDto: orderSheet.getCartDtoList()){
            cartDtoMap.put(cartDto.getProductNo(), cartDto);
            productNoList.add(cartDto.getProductNo());
        }
    }
    public void addPointAccumulateSum(Long amount){
        pointAccumulateSum += amount;
    }
    public void addProductTotalPrice(Long price){
        productTotalPrice += price;
    }

    public void addProductDiscountedTotalPrice(Long discountedPrice){
        productDiscountedTotalPrice += discountedPrice;
    }
    public void addOrderCouponDiscountSum(Long discountAmount){
        orderCouponDiscountSum += discountAmount;
    }
}
