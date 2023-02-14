package com.nhnacademy.booklay.server.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderSheetSaveResponse {
    private String orderId;
    private Boolean valid;
    private Long paymentAmount;
}
