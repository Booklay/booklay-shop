package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.request.DisAndConnectBookWithSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.io.IOException;
import java.util.List;

public interface BookSubscribeService {
    void bookSubscribeConnection(DisAndConnectBookWithSubscribeRequest request);

    void bookSubscribeDisconnection(DisAndConnectBookWithSubscribeRequest request);

    List<RetrieveProductResponse> retrieveBookSubscribe(Long subscribeId) throws IOException;
}
