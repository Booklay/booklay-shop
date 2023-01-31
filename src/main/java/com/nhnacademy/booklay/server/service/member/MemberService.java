package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.reponse.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberAuthorityUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
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

    void blockMember(Long memberNo, MemberBlockRequest request);

    Page<BlockedMemberRetrieveResponse> retrieveBlockedMember(Pageable pageable);

    Optional<MemberRetrieveResponse> retrieveMemberByEmail(String memberId);
}
