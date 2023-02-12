package com.nhnacademy.booklay.server.service.delivery;

import com.nhnacademy.booklay.server.dto.order.OrderRecipe;
import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;

public interface DeliveryDetailService {
    OrderRecipe retrieveOrderRecipe(Long memberNo, Long orderNo);

    DeliveryDetail saveDeliveryDetailFromOrderSheet(OrderSheet orderSheet, Long orderNo);

    void deleteDeliveryDetail(Long deliveryDetailNo);
}
