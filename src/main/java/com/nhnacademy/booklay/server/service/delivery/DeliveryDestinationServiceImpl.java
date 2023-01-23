package com.nhnacademy.booklay.server.service.delivery;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryDestinationRepositoryRepository;
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
    private final DeliveryDestinationRepositoryRepository deliveryDestinationRepository;
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
            .orElseThrow(() -> new IllegalArgumentException());

        return DeliveryDestinationRetrieveResponse.fromEntity(deliveryDestination);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestinations(Long memberNo) {
        memberRepository.findById(memberNo)
            .orElseThrow(() -> new MemberNotFoundException(memberNo));
        return deliveryDestinationRepository.retrieveDeliveryDestinationByMemberNo(memberNo);
    }

    private DeliveryDestination checkExistsDeliveryDestination(Long addressNo) {
        //TODO 3: 존재하지 않는 배송지
        return deliveryDestinationRepository.findById(addressNo)
            .orElseThrow(() -> new IllegalArgumentException());
    }

    @Override
    public void updateDeliveryDestination(Long memberNo, Long addressNo,
                                          DeliveryDestinationCURequest requestDto) {
        getMemberService.getMember(memberNo);
        DeliveryDestination deliveryDestination = checkExistsDeliveryDestination(addressNo);

        releaseDefaultDeliveryDestination(requestDto.getIsDefaultDestination());

        deliveryDestination.update(requestDto);
    }

    @Override
    public void deleteDeliveryDestination(Long memberNo, Long addressNo) {
        getMemberService.getMember(memberNo);
        DeliveryDestination deliveryDestination = checkExistsDeliveryDestination(addressNo);

        deliveryDestinationRepository.delete(deliveryDestination);
    }


    private void checkDeliveryDestinationLimit(Long memberNo) {
        if (deliveryDestinationRepository.countByMember_MemberNo(memberNo) > 10) {
            throw new IllegalArgumentException(); //TODO 3: 배송지 10개 넘을 수 없음
        }
    }

}
