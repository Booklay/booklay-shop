package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 양승아
 */
public interface MemberService {
    MemberRetrieveResponse retrieveMember(Long memberNo);

    Page<MemberRetrieveResponse> retrieveMembers(Pageable pageable);

    void createMember(MemberCreateRequest memberCreateRequest);

    void updateMember(Long memberNo, MemberUpdateRequest memberUpdateRequest);

    void deleteMember(Long memberNo);

    void createMemberAuthority(Long memberNo, String authority);
}
