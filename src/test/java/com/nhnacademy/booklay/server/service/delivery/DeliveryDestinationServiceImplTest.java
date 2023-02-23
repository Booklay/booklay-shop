package com.nhnacademy.booklay.server.service.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.delivery.DeliveryDestinationNotFoundException;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryDestinationRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.service.member.GetMemberService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author 양승아
 */
@ExtendWith(MockitoExtension.class)
class DeliveryDestinationServiceImplTest {
    @InjectMocks
    DeliveryDestinationServiceImpl deliveryDestinationService;
    @Mock
    DeliveryDestinationRepository deliveryDestinationRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    GetMemberService getMemberService;
    DeliveryDestination deliveryDestination;
    DeliveryDestinationCURequest requestDto;
    Member member;

    @BeforeEach
    void setUp() {
        member = Dummy.getDummyMember();
        deliveryDestination = Dummy.getDummyDeliveryDestination();
        requestDto = Dummy.getDummyDeliveryDestinationCreateRequest();
    }

    @Test
    @DisplayName("배송지 생성 테스트")
    void createMemberSuccessTest() {
        //given
        given(memberRepository.findByMemberNo(any())).willReturn(Optional.of(member));

        //when
        deliveryDestinationService.createDeliveryDestination(any(), requestDto);

        //then
        then(deliveryDestinationRepository).should().save(any(DeliveryDestination.class));
    }

    @Test
    @DisplayName("회원 배송지 조회 시 한 개 반환 성공 테스트")
    void retrieveDeliveryDestinationSuccessTest() {
        //given
        given(deliveryDestinationRepository.findById(any())).willReturn(
            Optional.of(deliveryDestination));

        //when
        deliveryDestinationService.retrieveDeliveryDestination(any());

        //then
        then(deliveryDestinationRepository).should().findById(any());
    }

    @Test
    @DisplayName("회원 배송지 조회 시 한 개 반환 실패 테스트")
    void retrieveDeliveryDestinationFailTest() {
        //given
        given(deliveryDestinationRepository.findById(any())).willThrow(
            new DeliveryDestinationNotFoundException(any()));

        //when

        //then
        assertThatThrownBy(() -> deliveryDestinationService.retrieveDeliveryDestination(
            deliveryDestination.getId()))
            .isInstanceOf(DeliveryDestinationNotFoundException.class);
    }

    @Test
    @DisplayName("회원 배송지 조회 시 List 반환 성공 테스트")
    void retrieveDeliveryDestinationsSuccessTest() {
        //given
        given(
            deliveryDestinationRepository.retrieveDeliveryDestinationByMemberNo(any())).willReturn(
            List.of());

        //when
        List<DeliveryDestinationRetrieveResponse> responses =
            deliveryDestinationService.retrieveDeliveryDestinations(any());

        //then
        then(deliveryDestinationRepository).should().retrieveDeliveryDestinationByMemberNo(any());
        assertThat(responses.size()).isZero();
    }

    @Test
    @DisplayName("배송지 번호로 배송지 반환 성공 테스트")
    void checkExistsDeliveryDestinationSuccessTest() {
        //given
        given(deliveryDestinationRepository.findById(any())).willReturn(
            Optional.of(deliveryDestination));

        //when
        deliveryDestinationService.checkExistsDeliveryDestination(any());

        //then
        then(deliveryDestinationRepository).should().findById(any());
    }

    @Test
    @DisplayName("배송지 수정 성공 테스트")
    void updateDeliveryDestinationSuccessTest() {
        //given
        given(getMemberService.getValidMemberByMemberNo(any())).willReturn(member);
        given(deliveryDestinationRepository.findById(any())).willReturn(
            Optional.of(deliveryDestination));
        given(
            deliveryDestinationRepository.findByIsDefaultDestinationAndMember_MemberNo(true,
                member.getMemberNo()))
            .willReturn(Optional.of(deliveryDestination));
        //when
        deliveryDestinationService.updateDeliveryDestination(member.getMemberNo(),
            deliveryDestination.getId(), requestDto);

        //then
        then(deliveryDestinationRepository).should().save(deliveryDestination);
    }

    @Test
    @DisplayName("배송지 삭제 성공 테스트")
    void deleteDeliveryDestination() {
        //given
        given(getMemberService.getValidMemberByMemberNo(any())).willReturn(member);
        given(deliveryDestinationRepository.findById(any())).willReturn(
            Optional.of(deliveryDestination));

        //when
        deliveryDestinationService.deleteDeliveryDestination(member.getMemberNo(),
            deliveryDestination.getId());

        //then
        then(deliveryDestinationRepository).should().delete(any());
    }
}