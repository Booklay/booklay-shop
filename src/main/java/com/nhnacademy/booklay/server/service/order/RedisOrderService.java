package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.order.OrderSheet;

public interface RedisOrderService {

    String saveOrderSheet(OrderSheet orderSheet);

    OrderSheet retrieveOrderSheet(String orderId);
}
