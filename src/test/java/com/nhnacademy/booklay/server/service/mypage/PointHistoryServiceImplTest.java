package com.nhnacademy.booklay.server.service.mypage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.PointPresentRequest;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.PointHistory;
import com.nhnacademy.booklay.server.repository.mypage.PointHistoryRepository;
import com.nhnacademy.booklay.server.service.member.GetMemberService;
import com.nhnacademy.booklay.server.service.mypage.impl.PointHistoryServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author 양승아
 */
@ExtendWith(MockitoExtension.class)
class PointHistoryServiceImplTest {
    @InjectMocks
    PointHistoryServiceImpl pointHistoryService;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    GetMemberService getMemberService;

    PointHistory pointHistory;
    Member member;
    PointHistoryCreateRequest pointHistoryCreateRequest;
    TotalPointRetrieveResponse totalPointRetrieveResponse;
    PointPresentRequest pointPresentRequest;

    @BeforeEach
    void setUp() {
        member = Dummy.getDummyMember();
        pointHistory = Dummy.getDummyPointHistory();
        pointHistoryCreateRequest = Dummy.getDummyPointHistoryCreateRequest();

        totalPointRetrieveResponse = TotalPointRetrieveResponse.builder()
            .member(member.getMemberNo())
            .totalPoint(15000)
            .build();

        pointPresentRequest = PointPresentRequest.builder()
            .targetMemberId(member.getMemberId())
            .targetPoint(100)
            .build();
    }

    @Test
    @DisplayName("포인트 생성 테스트")
    void createPointSuccessTest() {
        //given
        given(getMemberService.getValidMemberByMemberNo(any())).willReturn(member);

        given(pointHistoryRepository.retrieveLatestPointHistory(member.getMemberNo())).willReturn(
            Optional.of(totalPointRetrieveResponse));

        //when
        pointHistoryService.createPointHistory(pointHistoryCreateRequest);

        //then
        BDDMockito.then(pointHistoryRepository).should().save(any());
    }

    @Test
    @DisplayName("특정 회원 포인트 조회 테스트")
    void retrievePointHistoriesSuccessTest() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        given(pointHistoryRepository.retrievePointHistoryByMemberNo(member.getMemberNo(),
            pageRequest)).willReturn(Page.empty());

        given(getMemberService.getValidMemberByMemberNo(any())).willReturn(member);

        //when
        pointHistoryService.retrievePointHistorys(member.getMemberNo(), pageRequest);

        //then
        BDDMockito.then(pointHistoryRepository).should()
            .retrievePointHistoryByMemberNo(member.getMemberNo(), pageRequest);
    }

    @Test
    @DisplayName("특정 회원 현재 누적 포인트 조회 테스트")
    void retrieveTotalPointSuccessTest() {
        //given
        given(getMemberService.getValidMemberByMemberNo(any())).willReturn(member);

        given(pointHistoryRepository.retrieveLatestPointHistory(any())).willReturn(
            Optional.of(totalPointRetrieveResponse));

        //when
        pointHistoryService.retrieveTotalPoint(member.getMemberNo());

        //then
        BDDMockito.then(pointHistoryRepository).should().retrieveLatestPointHistory(any());
    }

    @Test
    @DisplayName("포인트 선물하기 테스트")
    void presentPointSuccessTest() {
        //given
        given(getMemberService.getValidMemberByMemberNo(any())).willReturn(member);
        given(getMemberService.getValidMemberByMemberId(any())).willReturn(member);
        given(pointHistoryRepository.retrieveLatestPointHistory(any())).willReturn(
            Optional.of(totalPointRetrieveResponse));

        //when
        pointHistoryService.presentPoint(member.getMemberNo(), pointPresentRequest);

        //then
    }

}