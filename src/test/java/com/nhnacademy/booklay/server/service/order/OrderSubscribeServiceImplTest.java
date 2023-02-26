package com.nhnacademy.booklay.server.service.order;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.dto.order.payment.SubscribeDto;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.OrderSubscribe;
import com.nhnacademy.booklay.server.repository.order.OrderSubscribeRepository;
import java.time.LocalDate;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class OrderSubscribeServiceImplTest {

  @InjectMocks
  OrderSubscribeServiceImpl orderSubscribeService;
  @Mock
  OrderSubscribeRepository subscribeRepository;

  Long orderNo;
  Long orderSubscribeNo;
  Integer renewMonth;
  OrderSubscribe orderSubscribe;

  @BeforeEach
  void setUp() {
    orderNo = Dummy.getDummyOrder().getId();
    orderSubscribeNo = 1L;
    renewMonth = 6;
    orderSubscribe = OrderSubscribe.builder()
        .id(1L)
        .subscribe(DummyCart.getDummySubscribe(DummyCart.getDummyProductSubscribeDto()))
        .subscribeNo(1L)
        .order(Dummy.getDummyOrder())
        .orderNo(Dummy.getDummyOrder().getId())
        .amounts(6)
        .price(10000L)
        .startAt(LocalDate.of(2023, 03, 01))
        .finishAt(LocalDate.of(2024, 02, 28)).build();
  }

  @Test
  void retrieveOrderSubscribe() {
    given(subscribeRepository.findById(orderSubscribeNo)).willReturn(
        Optional.ofNullable(orderSubscribe));

    OrderSubscribe actual = orderSubscribeService.retrieveOrderSubscribe(orderSubscribeNo);

    assertThat(actual.getOrderNo()).isEqualTo(orderSubscribe.getOrderNo());
  }

  @Test
  void saveOrderSubscribe() {
    SubscribeDto subscribeDto = new SubscribeDto(1L,1,10000L);
    OrderSubscribe expect = OrderSubscribe.builder()
        .subscribeNo(subscribeDto.getSubscribeNo())
        .orderNo(orderNo)
        .amounts(subscribeDto.getCount())
        .price(subscribeDto.getPrice())
        .startAt(LocalDate.now())
        .finishAt(LocalDate.now().plusMonths(subscribeDto.getCount()))
        .build();

    orderSubscribeService.saveOrderSubscribe(subscribeDto, orderNo);

    then(subscribeRepository).should().save(any());
  }


  @Test
  void deleteOrderSubscribeByOrderNo() {
    //when
    orderSubscribeService.deleteOrderSubscribeByOrderNo(orderNo);

    //then
    then(subscribeRepository).should().deleteAllByOrderNo(orderNo);
  }
}