package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.entity.OrderStatusCode;

public interface OrderStatusService {
    String retrieveOrderStatusCodeName(Long orderStatusCode);

    OrderStatusCode saveOrderStatusCode(OrderStatusCode orderStatusCode);

    void deleteOrderStatusCode(Long orderStatusCodeNo);
}
