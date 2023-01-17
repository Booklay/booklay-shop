package com.nhnacademy.booklay.server.repository.delivery;

import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDestinationRepository extends JpaRepository<DeliveryDestination, Long> {
    List<DeliveryDestinationRetrieveResponse> findAllByMember_MemberNo(Long memberNo);

    int countByMember_MemberNo(Long memberNo);
}
