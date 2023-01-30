package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.request.DisAndConnectBookWithSubscribeRequest;

public interface BookSubscribeService {
  void bookSubscribeConnection(DisAndConnectBookWithSubscribeRequest request);

  void bookSubscribeDisconnection(DisAndConnectBookWithSubscribeRequest request);
}
