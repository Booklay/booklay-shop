package com.nhnacademy.booklay.server.service.delivery;

import com.nhnacademy.booklay.server.dto.order.OrderRecipe;
import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryDetailRepository;
import com.nhnacademy.booklay.server.service.order.OrderService;
import com.nhnacademy.booklay.server.service.order.OrderStatusService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryDetailServiceImpl implements DeliveryDetailService{
    private final DeliveryDetailRepository deliveryDetailRepository;
    private final OrderStatusService orderStatusService;
    private final DeliveryStatusCodeService deliveryStatusCodeService;
    private final OrderService orderService;

    @Override
    public OrderRecipe retrieveOrderRecipe(Long memberNo, Long orderNo){
        OrderRecipe orderRecipe = deliveryDetailRepository.findByOrder_MemberNoAndOrder_Id(memberNo, orderNo).orElse(OrderRecipe.builder().build());
        orderRecipe.setOrderStatus(orderStatusService.retrieveOrderStatusCodeName(orderRecipe.getOrderStatusNo()));
        orderRecipe.setDeliveryStatus(deliveryStatusCodeService.retrieveOrderStatusCodeName(orderRecipe.getDeliveryStatusNo()));
        return orderRecipe;
    }

    @Override
    public DeliveryDetail saveDeliveryDetailFromOrderSheet(OrderSheet orderSheet, Long orderNo){
        return deliveryDetailRepository.save(DeliveryDetail.builder()
                .orderNo(orderNo)
                .sender(orderSheet.getSender())
                .senderPhoneNumber(orderSheet.getSenderPhoneNo())
                .address(orderSheet.getAddress())
                .receiver(orderSheet.getReceiver())
                .receiverPhoneNumber(orderSheet.getReceiverPhoneNo())
                .zipCode(orderSheet.getZipCode())
                .deliveryStatusCodeNo(1)
                .memo(orderSheet.getMemo())
                .build());
    }

    @Override
    public void deleteDeliveryDetail(Long deliveryDetailNo){
        deliveryDetailRepository.deleteById(deliveryDetailNo);
    }

    /**
     * todo 쿠폰 반환은 프론트에서 따로 쏴주게 하기
     */
    public void refundDelivery(Long memberNo, Long orderNo){
        List<DeliveryDetail> deliveryDetailList = deliveryDetailRepository.findAllByOrder_MemberNoAndOrder_Id(memberNo, orderNo);
        if (deliveryDetailList.isEmpty()){
            return;
        }
        deliveryDetailRepository.deleteAllById(
            deliveryDetailList.stream().map(DeliveryDetail::getId)
                .collect(Collectors.toList()));
        orderService.deleteOrder(orderNo);
    }
}
