package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.OrderSheetSaveResponse;

public interface RedisOrderService {

    OrderSheetSaveResponse saveOrderSheet(OrderSheet orderSheet);

    OrderSheet retrieveOrderSheet(String orderId);
}
