package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.request.MemberAuthorityUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.response.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberChartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 양승아
 */
public interface MemberService {
    void checkExistsMemberId(String memberId);

    MemberRetrieveResponse retrieveMember(Long memberNo);

    Page<MemberRetrieveResponse> retrieveMembers(Pageable pageable);

    void createMember(MemberCreateRequest memberCreateRequest);

    void updateMember(Long memberNo, MemberUpdateRequest memberUpdateRequest);

    void deleteMember(Long memberNo);

    void createMemberAuthority(Long memberNo, MemberAuthorityUpdateRequest request);

    Optional<MemberLoginResponse> retrieveMemberById(String memberId);

    void deleteMemberAuthority(Long memberNo, String authorityName);

    void createMemberGrade(Long memberNo, String gradeName);

    Page<MemberGradeRetrieveResponse> retrieveMemberGrades(Long memberNo, Pageable pageable);

    void createBlockMember(Long memberNo, MemberBlockRequest request);

    Page<BlockedMemberRetrieveResponse> retrieveBlockedMember(Pageable pageable);

    Optional<MemberRetrieveResponse> retrieveMemberByEmail(String memberId);

    void blockMemberCancel(Long blockedMemberDetailId);

    Page<BlockedMemberRetrieveResponse> retrieveBlockedMemberDetail(Long memberNo,
                                                                    Pageable pageable);

    Page<DroppedMemberRetrieveResponse> retrieveDroppedMembers(Pageable pageable);

    List<MemberAuthorityRetrieveResponse> retrieveMemberAuthority(Long memberNo);

    MemberChartRetrieveResponse retrieveMemberChart();
}
