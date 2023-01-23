package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.member.reponse.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.PointHistory;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.mypage.PointHistoryRepository;
import com.nhnacademy.booklay.server.service.member.GetMemberService;
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
    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final GetMemberService getMemberService;

    @Override
    public Page<PointHistoryRetrieveResponse> retrievePointHistorys(Pageable pageable) {
        return pointHistoryRepository.findAllBy(pageable);
    }

    @Override
    public void createPointHistory(PointHistoryCreateRequest requestDto) {
        Member member = getMemberService.getMember(requestDto.getMemberNo());

        PointHistory recentlyPointHistory =
            pointHistoryRepository.findPointHistoryByOrderByUpdatedAtDesc()
                .orElseThrow(() -> new IllegalArgumentException());

        Integer currentTotalPoint = recentlyPointHistory.getTotalPoint();

        PointHistory pointHistory = requestDto.toEntity(member, currentTotalPoint);

        pointHistoryRepository.save(pointHistory);
    }
}
