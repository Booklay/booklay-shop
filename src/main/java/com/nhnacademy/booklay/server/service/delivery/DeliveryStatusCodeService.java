package com.nhnacademy.booklay.server.service.delivery;

import com.nhnacademy.booklay.server.entity.DeliveryStatusCode;

public interface DeliveryStatusCodeService {
    String retrieveOrderStatusCodeName(Integer deliveryStatusCodeNo);

    DeliveryStatusCode saveOrderStatusCode(DeliveryStatusCode deliveryStatusCode);

    void deleteOrderStatusCode(Integer deliveryStatusCodeNo);
}
