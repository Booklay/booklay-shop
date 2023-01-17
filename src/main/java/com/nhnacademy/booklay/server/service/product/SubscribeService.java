package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.Subscribe;

/**
 * @author 최규태
 */

public interface SubscribeService {
  Subscribe createSubscribe(Subscribe subscribe);

  void updateSubscribeById(Subscribe subscribe);
}
