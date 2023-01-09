package com.nhnacademy.booklay.server.service.product.impl;


import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {
  private final SubscribeRepository subscribeRepository;

  @Override
  public Subscribe createSubscribe(Subscribe subscribe) {
    return subscribeRepository.save(subscribe);
  }

  @Override
  public Subscribe updateSubscribeById(Long id, Subscribe subscribe) {
    return subscribeRepository.updateSubscribeById(id, subscribe);
  }
}
