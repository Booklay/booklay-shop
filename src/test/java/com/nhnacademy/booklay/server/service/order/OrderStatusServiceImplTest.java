package com.nhnacademy.booklay.server.service.order;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.entity.OrderStatusCode;
import com.nhnacademy.booklay.server.repository.order.OrderStatusCodeRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @Author : 최규태
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class OrderStatusServiceImplTest {

  @InjectMocks
  OrderStatusServiceImpl orderStatusService;
  @Mock
  OrderStatusCodeRepository orderStatusCodeRepository;

  Map<Long, String> cachedCode;
  Long orderStatusCodeNo;
  OrderStatusCode orderStatusCode;

  @BeforeEach
  void setUp() {
    orderStatusCodeNo = 1L;
    cachedCode = new HashMap<>();
    cachedCode.put(orderStatusCodeNo, "결제완료");
    orderStatusCode = new OrderStatusCode(1L, "결제 완료");
  }

  @Test
  void retrieveOrderStatusCodeName() {
  }

  @Test
  void saveOrderStatusCode() {
    given(orderStatusCodeRepository.save(orderStatusCode)).willReturn(orderStatusCode);

    //when
    OrderStatusCode actual = orderStatusService.saveOrderStatusCode(orderStatusCode);

    assertThat(actual.getId()).isEqualTo(orderStatusCode.getId());
    assertThat(actual.getName()).isEqualTo(orderStatusCode.getName());
  }

  @Test
  void deleteOrderStatusCode() {
    //when
    orderStatusService.deleteOrderStatusCode(orderStatusCodeNo);

    then(orderStatusCodeRepository).should().deleteById(orderStatusCodeNo);
  }
}