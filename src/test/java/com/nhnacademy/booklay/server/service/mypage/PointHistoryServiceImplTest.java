package com.nhnacademy.booklay.server.service.mypage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.PointHistory;
import com.nhnacademy.booklay.server.repository.mypage.PointHistoryRepository;
import com.nhnacademy.booklay.server.service.member.GetMemberService;
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

    @BeforeEach
    void setUp() {
        member = Dummy.getDummyMember();
        pointHistory = Dummy.getDummyPointHistory();
        pointHistoryCreateRequest = Dummy.getDummyPointHistoryCreateRequest();

        totalPointRetrieveResponse = TotalPointRetrieveResponse.builder()
            .member(member.getMemberNo())
            .totalPoint(15000)
            .build();
    }

    @Disabled
    @Test
    @DisplayName("포인트 생성 테스트")
    void testCreatePoint() {
        //given
        given(getMemberService.getMemberNo(any())).willReturn(member);
        given(pointHistoryRepository.retrieveLatestPointHistory(member.getMemberNo())).willReturn(
            Optional.of(totalPointRetrieveResponse));

        //when
        pointHistoryService.createPointHistory(pointHistoryCreateRequest);
    }

}