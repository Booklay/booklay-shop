package com.nhnacademy.booklay.server.service.delivery;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.delivery.DeliveryDestinationLimitExceededException;
import com.nhnacademy.booklay.server.exception.delivery.DeliveryDestinationNotFoundException;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryDestinationRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryDestinationServiceImpl implements DeliveryDestinationService {
    private final DeliveryDestinationRepository deliveryDestinationRepository;
    private final MemberRepository memberRepository;

    @Override
    public void createDeliveryDestination(Long memberNo,
                                          DeliveryDestinationCURequest requestDto) {
        Member member = memberRepository.findByMemberNo(memberNo)
                                        .orElseThrow(() -> new MemberNotFoundException(memberNo));

        checkDeliveryDestinationLimit(memberNo);
        if (requestDto.getIsDefaultDestination()){
            releaseDefaultDeliveryDestination(memberNo);
        }

        DeliveryDestination deliveryDestination = requestDto.toEntity(member);

        deliveryDestinationRepository.save(deliveryDestination);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryDestinationRetrieveResponse retrieveDeliveryDestination(Long addressNo) {
        return DeliveryDestinationRetrieveResponse.fromEntity(retrieveDeliveryDestinationEntity(addressNo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestinations(Long memberNo) {
        return deliveryDestinationRepository.retrieveDeliveryDestinationByMemberNo(memberNo);
    }

    @Override
    public void updateDeliveryDestination(Long memberNo, Long addressNo,
                                          DeliveryDestinationCURequest requestDto) {
        DeliveryDestination deliveryDestination = retrieveDeliveryDestinationEntity(addressNo, memberNo);
        if (Boolean.FALSE.equals(deliveryDestination.getIsDefaultDestination()) &&
                requestDto.getIsDefaultDestination()){
            releaseDefaultDeliveryDestination(memberNo);
        }
        deliveryDestination.update(requestDto);
        deliveryDestinationRepository.save(deliveryDestination);
    }

    @Override
    public void deleteDeliveryDestination(Long memberNo, Long addressNo) {
        deliveryDestinationRepository.deleteByIdAndMemberNo(memberNo, addressNo);
    }

    @Override
    public void deleteAllDeliveryDestination(Long memberNo) {
        deliveryDestinationRepository.deleteAllByMemberNo(memberNo);
    }

    private void releaseDefaultDeliveryDestination(Long memberNo) {
        Optional<DeliveryDestination> deliveryDestinationOp =
                deliveryDestinationRepository.findByMemberNoAndIsDefaultDestinationIsTrue(memberNo);
        deliveryDestinationOp.ifPresent(deliveryDestination -> {
            deliveryDestination.setIsDefaultDestination(false);
            deliveryDestinationRepository.save(deliveryDestination);
        });
    }

    @Transactional(readOnly = true)
    public DeliveryDestination retrieveDeliveryDestinationEntity(Long addressNo) {
        return deliveryDestinationRepository.findById(addressNo)
                .orElseThrow(() -> new DeliveryDestinationNotFoundException(addressNo));
    }
    @Transactional(readOnly = true)
    public DeliveryDestination retrieveDeliveryDestinationEntity(Long addressNo, Long memberNo) {
        return deliveryDestinationRepository.findByIdAndMemberNo(addressNo, memberNo)
                .orElseThrow(() -> new DeliveryDestinationNotFoundException(addressNo));
    }


    private void checkDeliveryDestinationLimit(Long memberNo) {
        if (deliveryDestinationRepository.countByMemberNo(memberNo) >= 10) {
            throw new DeliveryDestinationLimitExceededException();
        }
    }


}
