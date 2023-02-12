package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.order.SubscribeDto;
import com.nhnacademy.booklay.server.entity.OrderSubscribe;

public interface OrderSubscribeService {
    OrderSubscribe retrieveOrderSubscribe(Long orderSubscribeNo);

    OrderSubscribe saveOrderSubscribe(SubscribeDto subscribeDto, Long orderNo);

    OrderSubscribe renewOrderSubscribe(Long subScribeNo, Integer renewDay);
}
