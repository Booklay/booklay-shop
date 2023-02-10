package com.nhnacademy.booklay.server.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCheckResponse {
    Boolean valid;
    Integer paymentAmount;
}
