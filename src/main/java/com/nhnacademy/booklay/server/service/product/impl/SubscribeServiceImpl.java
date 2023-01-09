package com.nhnacademy.booklay.server.service.product.impl;


import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

  private final SubscribeRepository subscribeRepository;

  @Override
  @Transactional
  public Subscribe createSubscribe(Subscribe subscribe) {
    return subscribeRepository.save(subscribe);
  }

  @Override
  @Transactional
  public void updateSubscribeById(Subscribe subscribe) {
    if (!subscribeRepository.existsById(subscribe.getId())) {
      throw new IllegalArgumentException();
    }
    subscribeRepository.save(subscribe);

  }
}
