package com.nhnacademy.booklay.server.service.delivery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyNum3;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.entity.DeliveryStatusCode;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryDetailRepository;
import com.nhnacademy.booklay.server.service.order.OrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class DeliveryDetailServiceImplTest {

  @InjectMocks
  DeliveryDetailServiceImpl deliveryDetailService;
  @Mock
  DeliveryDetailRepository deliveryDetailRepository;
  @Mock
  OrderService orderService;

  Long memberNo;
  Long orderNo;
  Long deliveryDetailNo;
  OrderSheet orderSheet;
  DeliveryDetail deliveryDetail;

  @BeforeEach
  void setup() {
    memberNo = 1L;
    orderNo = 1L;
    deliveryDetailNo = 1L;
    orderSheet = new OrderSheet();
    deliveryDetail = Dummy.getDummyDeliveryDetail();
  }
  @Test
  void retrieveDeliveryDetailByMemberNoAndOrderNo() {

    deliveryDetailService.retrieveDeliveryDetailByMemberNoAndOrderNo(memberNo, orderNo);

    then(deliveryDetailRepository).should().findAllByOrder_MemberNoAndOrder_Id(memberNo, orderNo);

  }

  @Test
  void saveDeliveryDetailFromOrderSheet() {
    deliveryDetailService.saveDeliveryDetailFromOrderSheet(orderSheet, orderNo);

    then(deliveryDetailRepository).should().save(any());
  }

  @Test
  void deleteDeliveryDetail() {
    deliveryDetailService.deleteDeliveryDetail(deliveryDetailNo);
    then(deliveryDetailRepository).should().deleteById(deliveryDetailNo);
  }

  @Test
  void refundDelivery() {
    List<DeliveryDetail> deliveryDetailList = new ArrayList<>();
    deliveryDetailList.add(deliveryDetail);

    given(deliveryDetailRepository.findAllByOrder_MemberNoAndOrder_Id(memberNo, orderNo)).willReturn(deliveryDetailList);
    if (deliveryDetailList.isEmpty()) {
      return;
    }

    deliveryDetailService.refundDelivery(memberNo, orderNo);

    then(deliveryDetailRepository).should().deleteAllById(deliveryDetailList.stream().map(DeliveryDetail::getId)
        .collect(Collectors.toList()));
    then(orderService).should().deleteOrder(orderNo);


  }
}