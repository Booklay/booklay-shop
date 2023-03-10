package com.nhnacademy.booklay.server.service.mypage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
 * @author μμΉμ
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
    @DisplayName("ν¬μΈνΈ μμ± νμ€νΈ")
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
    @DisplayName("νΉμ  νμ ν¬μΈνΈ μ‘°ν νμ€νΈ")
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
    @DisplayName("νΉμ  νμ νμ¬ λμ  ν¬μΈνΈ μ‘°ν νμ€νΈ")
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
    @DisplayName("ν¬μΈνΈ μ λ¬ΌνκΈ° νμ€νΈ")
    void presentPointSuccessTest() {
        //given
        given(getMemberService.getValidMemberByMemberNo(any())).willReturn(member);
        given(getMemberService.getValidMemberByMemberId(any())).willReturn(member);
        given(pointHistoryRepository.retrieveLatestPointHistory(any())).willReturn(
            Optional.of(totalPointRetrieveResponse));

        //when
        pointHistoryService.presentPoint(member.getMemberNo(), pointPresentRequest);

        //then
        then(pointHistoryRepository).should(times(2)).retrieveLatestPointHistory(any());

    }

}