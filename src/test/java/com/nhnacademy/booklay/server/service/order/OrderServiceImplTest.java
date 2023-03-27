package com.nhnacademy.booklay.server.service.order;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.order.response.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.repository.order.OrderRepository;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @Author: 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @InjectMocks
  OrderServiceImpl orderService;
  @Mock
  OrderRepository orderRepository;
  @Mock
  OrderStatusService orderStatusService;
  @Mock
  PointHistoryService pointHistoryService;

  Long orderNo;
  Long memberNo;
  Pageable pageable;

  @BeforeEach
  void setUp() {
    orderNo = 1L;
    memberNo = 1L;
    pageable = PageRequest.of(0, 20);

  }

  @Test
  void retrieveOrder() {
    orderService.retrieveOrder(orderNo);

    then(orderRepository).should().findById(orderNo);
  }

  @Test
  void retrieveOrderListRetrieveResponsePageByMemberNoAndBlind() {
    OrderListRetrieveResponse orderlist = new OrderListRetrieveResponse(Dummy.getDummyOrder());
    List<OrderListRetrieveResponse> content = new ArrayList<>();
    content.add(orderlist);
    Page<OrderListRetrieveResponse> page = new PageImpl<>(content);
    given(orderRepository.findAllByMemberNoAndIsBlindedOrderByOrderedAtDesc(memberNo, false, pageable)).willReturn(
        page);

    PageResponse<OrderListRetrieveResponse> orderListRetrieveResponsePageResponse = new PageResponse<>(
        page);

    orderListRetrieveResponsePageResponse.getData().forEach(response ->
        response.setOrderStatusName(
            orderStatusService.retrieveOrderStatusCodeName(response.getOrderStatusCodeNo())));

    //when
    PageResponse<OrderListRetrieveResponse> actual =
        orderService.retrieveOrderListRetrieveResponsePageByMemberNoAndBlind(memberNo, false,
            pageable);

    assertThat(actual.getData()).hasSameSizeAs(orderListRetrieveResponsePageResponse.getData());
  }

  @Test
  void saveOrder() {
    OrderSheet sheet = new OrderSheet();

    orderService.saveOrder(sheet);

    then(orderRepository).should().save(any());

  }

  @Test
  void deleteOrder() {
    orderService.deleteOrder(orderNo);

    then(orderRepository).should().deleteById(orderNo);
  }

  @Test
  void confirmOrder() {
    Optional<Order> optionalOrder = Optional.of(Dummy.getDummyOrder());
    given(orderRepository.findByIdAndMemberNo(orderNo, memberNo)).willReturn(optionalOrder);

    Order order = optionalOrder.get();
    if (order.getPointAccumulate() != 0) {
      then(pointHistoryService).should().createPointHistory(new PointHistoryCreateRequest(order.getMemberNo(), order.getPointAccumulate(),
          "상품 구매 확정"));
    }
    order.setOrderStatusCodeNo(4L);
    given(orderRepository.save(order)).willReturn(order);

    Boolean result =orderService.confirmOrder(orderNo, memberNo);

    assertThat(result).isTrue();
  }
}