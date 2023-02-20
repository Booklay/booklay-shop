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

/**
 * @author 양승아
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryDestinationServiceImpl implements DeliveryDestinationService {
    private final DeliveryDestinationRepository deliveryDestinationRepository;
    private final MemberRepository memberRepository;
    private final GetMemberService getMemberService;

    /**
     * 기본 배송지 해제하는 메소드
     *
     * @param isDefaultDestination 기본 배송지 여부
     * @param memberNo             배송지의 주인
     */
    private void releaseDefaultDeliveryDestination(Boolean isDefaultDestination, Long memberNo) {
        Optional<DeliveryDestination> deliveryDestinationOp =
            deliveryDestinationRepository.findByIsDefaultDestinationAndMember_MemberNo(true,
                memberNo);
        if (deliveryDestinationOp.isPresent() && isDefaultDestination) {
            deliveryDestinationOp.get().setIsDefaultDestination(false);
        }
    }

    /**
     * 한 사람의 배송지 갯수는 10개까지만 가능
     *
     * @param memberNo 배송지를 가진 회원
     */
    private void checkDeliveryDestinationLimit(Long memberNo) {
        if (deliveryDestinationRepository.countByMember_MemberNo(memberNo) >= 10) {
            throw new DeliveryDestinationLimitExceededException();
        }
    }

    /**
     * 배송지 만드는 메소드
     *
     * @param memberNo   배송지 생성할 회원
     * @param requestDto 생성할 배송지 정보
     */
    @Override
    public void createDeliveryDestination(Long memberNo,
                                          DeliveryDestinationCURequest requestDto) {
        Member member = memberRepository.findByMemberNo(memberNo)
            .orElseThrow(() -> new MemberNotFoundException(memberNo));

        releaseDefaultDeliveryDestination(requestDto.getIsDefaultDestination(), memberNo);

        checkDeliveryDestinationLimit(memberNo);

        DeliveryDestination deliveryDestination = requestDto.toEntity(member);

        deliveryDestinationRepository.save(deliveryDestination);
    }

    /**
     * 배송지 한 개 조회하는 메소드
     *
     * @param addressNo 배송지 번호
     * @return DeliveryDestinationRetrieveResponse 반환
     */
    @Override
    @Transactional(readOnly = true)
    public DeliveryDestinationRetrieveResponse retrieveDeliveryDestination(Long addressNo) {
        DeliveryDestination deliveryDestination = deliveryDestinationRepository.findById(addressNo)
            .orElseThrow(
                () -> new DeliveryDestinationNotFoundException(addressNo));

        return DeliveryDestinationRetrieveResponse.fromEntity(deliveryDestination);
    }

    /**
     * 한 사람의 배송지 리스트 조회하는 메소드
     *
     * @param memberNo 배송지 주인
     * @return 배송지 리스트 반환
     */
    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDestinationRetrieveResponse> retrieveDeliveryDestinations(Long memberNo) {
        return deliveryDestinationRepository.retrieveDeliveryDestinationByMemberNo(memberNo);
    }

    /**
     * 배송지 한 개 조회 메소드
     *
     * @param addressNo 조회할 배송지 번호
     * @return DeliveryDestination 반환
     */
    @Transactional(readOnly = true)
    public DeliveryDestination checkExistsDeliveryDestination(Long addressNo) {
        return deliveryDestinationRepository.findById(addressNo)
            .orElseThrow(
                () -> new DeliveryDestinationNotFoundException(
                    addressNo));
    }

    /**
     * 배송지 수정 메소드
     *
     * @param memberNo   배송지 주인
     * @param addressNo  배송지 번호
     * @param requestDto 수정될 배송지 정보
     */
    @Override
    public void updateDeliveryDestination(Long memberNo, Long addressNo,
                                          DeliveryDestinationCURequest requestDto) {
        getMemberService.getValidMemberByMemberNo(memberNo);
        DeliveryDestination deliveryDestination = checkExistsDeliveryDestination(addressNo);

        releaseDefaultDeliveryDestination(requestDto.getIsDefaultDestination(), memberNo);

        deliveryDestination.update(requestDto);
    }

    /**
     * 배송지 삭제 메소드
     *
     * @param memberNo  배송지 삭제할 회원
     * @param addressNo 삭제될 배송지 번호
     */
    @Override
    public void deleteDeliveryDestination(Long memberNo, Long addressNo) {
        getMemberService.getValidMemberByMemberNo(memberNo);
        DeliveryDestination deliveryDestination = checkExistsDeliveryDestination(addressNo);

        deliveryDestinationRepository.delete(deliveryDestination);
    }


}
