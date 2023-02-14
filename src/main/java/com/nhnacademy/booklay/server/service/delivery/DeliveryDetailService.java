package com.nhnacademy.booklay.server.service.delivery;

import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import java.util.List;

public interface DeliveryDetailService {
    List<DeliveryDetail> retrieveDeliveryDetailByMemberNoAndOrderNo(Long memberNo, Long orderNo);

    DeliveryDetail saveDeliveryDetailFromOrderSheet(OrderSheet orderSheet, Long orderNo);

    void deleteDeliveryDetail(Long deliveryDetailNo);
}
