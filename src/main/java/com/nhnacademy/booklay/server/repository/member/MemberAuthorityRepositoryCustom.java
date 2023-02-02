package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberAuthorityRetrieveResponse;
import java.util.List;

public interface MemberAuthorityRepositoryCustom {
    List<MemberAuthorityRetrieveResponse> retrieveAuthoritiesByMemberNo(Long memberNo);
}
