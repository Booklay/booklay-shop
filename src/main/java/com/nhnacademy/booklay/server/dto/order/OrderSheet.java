package com.nhnacademy.booklay.server.dto.order;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderSheet {

    private List<String> couponCodeList;
    private List<CartDto> cartDtoList;
    @Setter
    private List<SubscribeDto> subscribeProductList;
    private Long productPriceSum;
    private Long usingPoint;
    private Long deliveryPrice;
    private Long giftWrappingPrice;
    private Long discountPrice;
    private Long paymentAmount;
    private Long paymentMethod;
    private String orderId;

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
//
//    public DeliveryDetail getDeliveryDetail(){
//
//        return DeliveryDetail.builder()
//            .order(getOrder())
//            .sender(sender)
//            .senderPhoneNumber(senderPhoneNo)
//            .address(address)
//            .receiver(receiver)
//            .receiverPhoneNumber(receiverPhoneNo)
//            .zipCode(zipCode)
//            .deliveryStatusCodeNo(1)
//            .memo(memo)
//            .build();
//
//    }
//
//    public Order getOrder(){
//        return Order.builder()
//            .memberNo(memberNo)
//            .orderStatusCodeNo(1L)
//            .productPriceSum(productPriceSum)
//            .deliveryPrice(deliveryPrice)
//            .discountPrice(discountPrice)
//            .pointUsePrice(usingPoint)
//            .paymentPrice(paymentAmount)
//            .paymentMethod(paymentMethod)
//            .giftWrappingPrice(giftWrappingPrice)
//            .isBlinded(Boolean.FALSE)
//            .build();
//    }

}
