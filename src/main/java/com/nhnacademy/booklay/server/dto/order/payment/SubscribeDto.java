package com.nhnacademy.booklay.server.dto.order.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeDto {
    Long subscribeNo;
    Integer count;
    Long price;
}
