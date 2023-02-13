package com.nhnacademy.booklay.server.service.mypage.impl;

import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.PointPresentRequest;
import com.nhnacademy.booklay.server.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.PointHistory;
import com.nhnacademy.booklay.server.repository.mypage.PointHistoryRepository;
import com.nhnacademy.booklay.server.service.member.GetMemberService;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 양승아
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;
    private final GetMemberService getMemberService;


    @Override
    public void createPointHistory(PointHistoryCreateRequest requestDto) {
        Member member = getMemberService.getMemberNo(requestDto.getMemberNo());

        TotalPointRetrieveResponse recentlyPointHistory =
            pointHistoryRepository.retrieveLatestPointHistory(requestDto.getMemberNo())
                                  .orElseGet(() -> new TotalPointRetrieveResponse(null, 0));

        Integer currentTotalPoint = recentlyPointHistory.getTotalPoint();

        if (currentTotalPoint + requestDto.getPoint() < 0) {
            throw new IllegalArgumentException();
        }
        PointHistory pointHistory = requestDto.toEntity(member, currentTotalPoint);

        pointHistoryRepository.save(pointHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PointHistoryRetrieveResponse> retrievePointHistorys(Long memberNo,
                                                                    Pageable pageable) {
        return pointHistoryRepository.retrievePointHistoryByMemberNo(memberNo, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public TotalPointRetrieveResponse retrieveTotalPoint(Long memberNo) {
        getMemberService.getMemberNo(memberNo);

        return pointHistoryRepository.retrieveLatestPointHistory(memberNo)
                                     .orElseGet(() -> new TotalPointRetrieveResponse(null, 0));
    }

    @Override
    public void presentPoint(Long memberNo, PointPresentRequest requestDto) {
        Member member = getMemberService.getMemberNo(memberNo);
        Member targetMember = getMemberService.getMemberId(requestDto.getTargetMemberId());

        createPointHistory(PointHistoryCreateRequest.builder()
                                                    .memberNo(memberNo)
                                                    .point(-(requestDto.getTargetPoint()))
                                                    .updatedDetail(
                                                        targetMember.getMemberId() + "에게 포인트 선물하기")
                                                    .build());

        createPointHistory(PointHistoryCreateRequest.builder()
                                                    .memberNo(targetMember.getMemberNo())
                                                    .point(requestDto.getTargetPoint())
                                                    .updatedDetail(
                                                        member.getMemberId() + "에게 포인트 선물받기")
                                                    .build());
    }

    @Override
    public void deleteAllByMemberNo(Long memberNo) {
        pointHistoryRepository.deleteAllByMemberNo(memberNo);
    }
}
