package com.nhnacademy.booklay.server.repository.delivery;

import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDestinationRepository
    extends JpaRepository<DeliveryDestination, Long>, DeliveryDestinationRepositoryCustom {
    Optional<DeliveryDestination> findByIsDefaultDestination(Boolean isDefaultDestination);

    int countByMember_MemberNo(Long memberNo);
    void deleteAllByMember_MemberNo(Long memberNo);
}
