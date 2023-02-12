package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.entity.Order;

public interface ComplexOrderService {
    OrderSheet checkOrder(OrderSheet orderSheet);

    Order saveReceipt(OrderSheet orderSheet);
}
