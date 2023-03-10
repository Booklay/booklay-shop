package com.nhnacademy.booklay.server.dto.order.payment;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.order.OrderProductDto;
import java.util.List;

import com.nhnacademy.booklay.server.dto.coupon.request.CouponUseRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class OrderSheet {
    private List<String> couponCodeList;
    private List<CartDto> cartDtoList;
    @Setter
    private List<OrderProductDto> orderProductDtoList;
    @Setter
    private List<SubscribeDto> subscribeProductList;
    @Setter
    private CouponUseRequest couponUseRequest;
    private Long productPriceSum;
    private Long usingPoint;
    private Long deliveryPrice;
    private Long giftWrappingPrice;
    private Long discountPrice;
    private Long paymentAmount;
    private Long paymentMethod;
    private String orderId;

    @Setter
    private Long memberNo;
    private String sender;
    private String senderPhoneNo;
    private String name;
    private String zipCode;
    private String address;
    private Boolean isDefaultDestination;
    private String receiver;
    private String receiverPhoneNo;
    private String memo;

    @Setter
    private Long orderNo;
    private String orderTitle;
    @Setter
    private Integer pointAccumulate;
}
