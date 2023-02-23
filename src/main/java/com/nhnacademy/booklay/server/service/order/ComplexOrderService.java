package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.order.payment.OrderReceipt;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.entity.Order;

public interface ComplexOrderService {
    OrderSheet checkOrder(OrderSheet orderSheet, MemberInfo memberInfo);

    Order saveReceipt(OrderSheet orderSheet, MemberInfo memberInfo);

    OrderReceipt retrieveOrderReceipt(Long orderNo, Long memberNo);
}
