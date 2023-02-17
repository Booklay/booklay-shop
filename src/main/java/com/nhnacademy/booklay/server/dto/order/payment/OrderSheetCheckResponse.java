package com.nhnacademy.booklay.server.dto.order.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderSheetCheckResponse extends OrderSheet {
    private String reason;
    private Integer reasonType;
    private Boolean success;
}
