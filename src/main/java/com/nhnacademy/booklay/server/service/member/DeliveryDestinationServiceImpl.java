package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCreateRequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.Member;
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
    public DeliveryDestinationRetrieveResponse retrieveDeliveryDestination(Long addressNo) {
        DeliveryDestination deliveryDestination = deliveryDestinationRepository.findById(addressNo)
            .orElseThrow(() -> new IllegalArgumentException());

        return DeliveryDestinationRetrieveResponse.fromEntity(deliveryDestination);
    }

    @Override
    public List<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestinations(Long memberNo) {
        memberRepository.findById(memberNo)
            .orElseThrow(() -> new MemberNotFoundException(memberNo));
        return deliveryDestinationRepository.findAllByMember_MemberNo(memberNo);
    }

    public void checkDeliveryDestinationLimit(Long memberNo) {
        if (deliveryDestinationRepository.countByMember_MemberNo(memberNo) > 10) {
            throw new IllegalArgumentException(); //TODO 3: 배송지 10개 넘을 수 없음
        }
    }

    @Override
    public void createDeliveryDestination(Long memberNo,
                                          DeliveryDestinationCreateRequest requestDto) {
        Optional<Member> memberOp = memberRepository.findByMemberNo(memberNo);
        if (memberOp.isEmpty()) {
            throw new MemberNotFoundException(memberNo);
        }
        checkDeliveryDestinationLimit(memberNo);

        DeliveryDestination deliveryDestination = requestDto.toEntity(memberOp.get());

        deliveryDestinationRepository.save(deliveryDestination);
    }

}
