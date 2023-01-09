package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.Subscribe;

public interface SubscribeService {
  Subscribe createSubscribe(Subscribe subscribe);

  Subscribe updateSubscribeById(Long id, Subscribe subscribe);
}
