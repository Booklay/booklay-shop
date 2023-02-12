package com.nhnacademy.booklay.server.dto.order;

import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCheckRequest {
    List<String> couponCodeList;
    List<CartDto> cartDtoList;
    Integer usingPoint;
    Integer giftWrappingPrice;
    Integer paymentAmount;
}
