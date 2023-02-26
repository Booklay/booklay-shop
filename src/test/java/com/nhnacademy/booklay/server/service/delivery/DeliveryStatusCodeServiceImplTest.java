package com.nhnacademy.booklay.server.service.delivery;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.DeliveryStatusCode;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryStatusCodeRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @Author: 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class DeliveryStatusCodeServiceImplTest {

  @InjectMocks
  DeliveryStatusCodeServiceImpl deliveryStatusCodeService;
  @Mock
  DeliveryStatusCodeRepository deliveryStatusCodeRepository;
  Integer deliveryStatusCodeNo;
  DeliveryStatusCode deliveryStatusCode;
  private Map<Integer, String> cachedCode;

  @BeforeEach
  void setup() {
    deliveryStatusCodeNo = 1;
    deliveryStatusCode = Dummy.getDummyDeliveryStatusCode();
  }

  @Test
  void retrieveOrderStatusCodeName() {
  }

  @Test
  void saveOrderStatusCode() {
    given(deliveryStatusCodeRepository.save(deliveryStatusCode)).willReturn(deliveryStatusCode);

    List<DeliveryStatusCode> deliveryStatusCodeList = new ArrayList<>();
    given(deliveryStatusCodeRepository.findAll()).willReturn(deliveryStatusCodeList);
    Map<Integer, String> newCachedCode = new HashMap<>();
    newCachedCode.put(null, null);
    deliveryStatusCodeList.forEach(
        deliveryStatusCode -> newCachedCode.put(deliveryStatusCode.getId(),
            deliveryStatusCode.getName()));
    cachedCode = newCachedCode;

    DeliveryStatusCode actual = deliveryStatusCodeService.saveOrderStatusCode(deliveryStatusCode);

    assertThat(actual.getName()).isEqualTo(deliveryStatusCode.getName());
  }

  @Test
  void deleteOrderStatusCode() {

    deliveryStatusCodeService.deleteOrderStatusCode(deliveryStatusCodeNo);


    then(deliveryStatusCodeRepository).should().deleteById(deliveryStatusCodeNo);

  }
}