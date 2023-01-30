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
import com.nhnacademy.booklay.server.service.member.GetMemberService;
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
    private final GetMemberService getMemberService;

    private void releaseDefaultDeliveryDestination(Boolean isDefaultDestination) {
        Optional<DeliveryDestination> deliveryDestinationOp =
            deliveryDestinationRepository.findByIsDefaultDestination(true);
        if (deliveryDestinationOp.isPresent() && isDefaultDestination) {
            deliveryDestinationOp.get().setIsDefaultDestination(false);
        }
    }

    @Override
    public void createDeliveryDestination(Long memberNo,
                                          DeliveryDestinationCURequest requestDto) {
        Member member = memberRepository.findByMemberNo(memberNo)
                                        .orElseThrow(() -> new MemberNotFoundException(memberNo));

        releaseDefaultDeliveryDestination(requestDto.getIsDefaultDestination());

        checkDeliveryDestinationLimit(memberNo);

        DeliveryDestination deliveryDestination = requestDto.toEntity(member);

        deliveryDestinationRepository.save(deliveryDestination);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryDestinationRetrieveResponse retrieveDeliveryDestination(Long addressNo) {
        DeliveryDestination deliveryDestination = deliveryDestinationRepository.findById(addressNo)
                                                                               .orElseThrow(
                                                                                   () -> new IllegalArgumentException());

        return DeliveryDestinationRetrieveResponse.fromEntity(deliveryDestination);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestinations(Long memberNo) {
        return deliveryDestinationRepository.retrieveDeliveryDestinationByMemberNo(memberNo);
    }

    @Transactional(readOnly = true)
    public DeliveryDestination checkExistsDeliveryDestination(Long addressNo) {
        return deliveryDestinationRepository.findById(addressNo)
                                            .orElseThrow(
                                                () -> new DeliveryDestinationNotFoundException(
                                                    addressNo));
    }

    @Override
    public void updateDeliveryDestination(Long memberNo, Long addressNo,
                                          DeliveryDestinationCURequest requestDto) {
        getMemberService.getMemberNo(memberNo);
        DeliveryDestination deliveryDestination = checkExistsDeliveryDestination(addressNo);

        releaseDefaultDeliveryDestination(requestDto.getIsDefaultDestination());

        deliveryDestination.update(requestDto);
    }

    @Override
    public void deleteDeliveryDestination(Long memberNo, Long addressNo) {
        getMemberService.getMemberNo(memberNo);
        DeliveryDestination deliveryDestination = checkExistsDeliveryDestination(addressNo);

        deliveryDestinationRepository.delete(deliveryDestination);
    }

    private void checkDeliveryDestinationLimit(Long memberNo) {
        if (deliveryDestinationRepository.countByMember_MemberNo(memberNo) >= 10) {
            throw new DeliveryDestinationLimitExceededException();
        }
    }


}
