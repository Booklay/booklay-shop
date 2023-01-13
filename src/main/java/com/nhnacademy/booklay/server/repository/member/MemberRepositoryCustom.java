package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MemberRepositoryCustom {

    MemberLoginResponse retrieveMemberByUserId(String userId);
}
