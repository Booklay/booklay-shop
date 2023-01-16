package com.nhnacademy.booklay.server.service.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCreateRequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryDestinationRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.service.member.DeliveryDestinationServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryDestinationServiceImplTest {
    @InjectMocks
    DeliveryDestinationServiceImpl deliveryDestinationService;
    @Mock
    DeliveryDestinationRepository deliveryDestinationRepository;
    @Mock
    MemberRepository memberRepository;
    DeliveryDestinationCreateRequest requestDto;
    Member member;

    @BeforeEach
    void setUp() {
        member = Dummy.getDummyMember();
        requestDto = Dummy.getDummyDeliveryDestinationCreateRequest();
    }

    @Disabled
    @Test
    @DisplayName("회원 배송지 조회 시 List 반환 성공 테스트")
    void retrieveDeliveryDestinations() {
        //given
        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        given(deliveryDestinationRepository.findAllByMember_MemberNo(any())).willReturn(List.of());

        //when
        List<DeliveryDestinationRetrieveResponse> responses =
            deliveryDestinationService.retrieveDeliveryDestinations(any());

        //then
        then(deliveryDestinationRepository).should().findAllByMember_MemberNo(any());
        assertThat(responses.size()).isZero();
    }

    @Disabled
    @Test
    @DisplayName("배송지 생성 테스트")
    void testCreateMember() {
        //given
        given(memberRepository.findByMemberNo(any())).willReturn(Optional.of(member));

        //when
        deliveryDestinationService.createDeliveryDestination(any(), requestDto);

        //then
        then(deliveryDestinationRepository).should().save(any(DeliveryDestination.class));
    }
}