package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.MemberRetrieveResponse;

import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 *
 * author 양승아
 */
public interface MemberService {
    MemberRetrieveResponse retrieveMember(Long memberNo);

    List<MemberRetrieveResponse> retrieveMembers(Pageable pageable);

    void createMember(MemberCreateRequest memberCreateRequest);
}
