package com.nhnacademy.booklay.server.repository.delivery;

import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDestinationRepository
    extends JpaRepository<DeliveryDestination, Long>, DeliveryDestinationRepositoryCustom {
    Optional<DeliveryDestination> findByIdAndMemberNo(Long addressId, Long memberNo);
    Optional<DeliveryDestination> findByMemberNoAndIsDefaultDestinationIsTrue(Long memberNo);
    int deleteByIdAndMemberNo(Long addressId, Long memberNo);
    int countByMemberNo(Long memberNo);
    void deleteAllByMemberNo(Long memberNo);
}
