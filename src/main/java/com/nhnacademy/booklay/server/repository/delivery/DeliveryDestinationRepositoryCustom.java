package com.nhnacademy.booklay.server.repository.delivery;

import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DeliveryDestinationRepositoryCustom {
    List<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestinationByMemberNo(Long memberNo);
}
