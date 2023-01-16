package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCreateRequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import java.util.List;

/**
 * @author 양승아
 */
public interface DeliveryDestinationService {
    List<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestinations(Long memberNo);

    void createDeliveryDestination(Long memberNo,
                                   DeliveryDestinationCreateRequest deliveryDestinationCreateRequest);
}
