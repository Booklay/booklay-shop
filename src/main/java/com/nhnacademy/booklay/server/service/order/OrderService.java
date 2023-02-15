package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.entity.Order;

public interface OrderService {
    Order retrieveOrder(Long orderNo);

    Order saveOrder(OrderSheet orderSheet);

    void deleteOrder(Long orderNo);

}
