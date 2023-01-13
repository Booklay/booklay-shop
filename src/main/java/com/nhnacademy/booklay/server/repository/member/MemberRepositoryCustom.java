package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface MemberRepositoryCustom {

    Optional<MemberLoginResponse> retrieveMemberByUserId(String userId);
}
