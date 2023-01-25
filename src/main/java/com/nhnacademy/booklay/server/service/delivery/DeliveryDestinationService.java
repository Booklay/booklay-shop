package com.nhnacademy.booklay.server.service.delivery;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import java.util.List;

/**
 * @author 양승아
 */
public interface DeliveryDestinationService {
    List<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestinations(Long memberNo);

    void createDeliveryDestination(Long memberNo,
                                   DeliveryDestinationCURequest deliveryDestinationCURequest);

    DeliveryDestinationRetrieveResponse retrieveDeliveryDestination(Long addressNo);

    void updateDeliveryDestination(Long memberNo, Long addressNo,
                                   DeliveryDestinationCURequest requestDto);

    void deleteDeliveryDestination(Long memberNo, Long addressNo);
}
