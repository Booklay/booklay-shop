package com.nhnacademy.booklay.server.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCheckResponse {
    Boolean valid;
    Integer paymentAmount;
}
