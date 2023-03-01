package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.request.MemberAuthorityUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author 양승아
 */
public interface MemberService {
    MemberRetrieveResponse retrieveMember(Long memberNo);

    Page<MemberRetrieveResponse> retrieveMembers(Pageable pageable);

    Long createMember(MemberCreateRequest memberCreateRequest);

    void updateMember(Long memberNo, MemberUpdateRequest memberUpdateRequest);

    void deleteMember(Long memberNo);

    void createMemberAuthority(Long memberNo, MemberAuthorityUpdateRequest request);

    Optional<MemberLoginResponse> retrieveMemberById(String memberId);

    void deleteMemberAuthority(Long memberNo, String authorityName);

    void createMemberGrade(Long memberNo, String gradeName);

    Page<MemberGradeRetrieveResponse> retrieveMemberGrades(Long memberNo, Pageable pageable);

    void createBlockMember(Long memberNo, MemberBlockRequest request);

    Page<BlockedMemberRetrieveResponse> retrieveBlockedMember(Pageable pageable);

    Optional<MemberLoginResponse> retrieveMemberByEmail(String memberId);

    Optional<MemberRetrieveResponse> retrieveMemberInfoByEmail(String email);
    
    void blockMemberCancel(Long blockedMemberDetailId);

    Page<BlockedMemberRetrieveResponse> retrieveBlockedMemberDetail(Long memberNo,
                                                                    Pageable pageable);

    Page<DroppedMemberRetrieveResponse> retrieveDroppedMembers(Pageable pageable);

    List<MemberAuthorityRetrieveResponse> retrieveMemberAuthority(Long memberNo);

    MemberChartRetrieveResponse retrieveMemberChart();

    MemberMainRetrieveResponse retrieveMemberMain(Long memberNo);

    boolean checkMemberId(String memberId);

    boolean checkNickName(String nickName);

    boolean checkEMail(String eMail);
}
