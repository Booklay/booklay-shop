package com.nhnacademy.booklay.server.dto.order.response;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderReceiptSaveResponse {
    private Long orderId;
    private List<CartDto> cartDtoList;
    private Boolean isError;
    private String errorMessage;

    public OrderReceiptSaveResponse(Long orderId, List<CartDto> cartDtoList, boolean isError) {
        this.orderId = orderId;
        this.cartDtoList = cartDtoList;
        this.isError = isError;
    }

    public OrderReceiptSaveResponse(Boolean isError, String errorMessage) {
        this.isError = isError;
        this.errorMessage = errorMessage;
    }
}
