package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.entity.Order;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order retrieveOrder(Long orderNo);

    PageResponse<OrderListRetrieveResponse> retrieveOrderListRetrieveResponsePageByMemberNoAndBlind(
        Long memberNo, Boolean isBlind, Pageable pageable);

    Order saveOrder(OrderSheet orderSheet);

    void deleteOrder(Long orderNo);

}
