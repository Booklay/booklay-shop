package com.nhnacademy.booklay.server.service.product.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @Author: 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class SubscribeServiceImplTest {
  @InjectMocks
  SubscribeServiceImpl subscribeService;
  @Mock
  SubscribeRepository subscribeRepository;
  @Test
  void retrieveSubscribeListByProductNoList() {
    subscribeService.retrieveSubscribeListByProductNoList(List.of());

    BDDMockito.then(subscribeRepository).should().findAllByProductNoIn(List.of());
  }

  @Test
  void retrieveSubscribeByProductNo() {
    Long subscribeId = 1L;
    subscribeService.retrieveSubscribeByProductNo(subscribeId);

    BDDMockito.then(subscribeRepository).should().findById(subscribeId);
  }
}