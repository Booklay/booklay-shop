package com.nhnacademy.booklay.server.service.delivery;

import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryDetailRepository;
import com.nhnacademy.booklay.server.service.order.OrderService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryDetailServiceImpl implements DeliveryDetailService{
    private final DeliveryDetailRepository deliveryDetailRepository;
    private final OrderService orderService;

    @Override
    public List<DeliveryDetail> retrieveDeliveryDetailByMemberNoAndOrderNo(Long memberNo, Long orderNo){
        return deliveryDetailRepository.findAllByOrder_MemberNoAndOrder_Id(memberNo, orderNo);
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
     *
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
