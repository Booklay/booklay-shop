package com.nhnacademy.booklay.server.dto.member.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberLoginResponse {

    private final String userId;

    private final String password;

    private final String authority;

    private final String email;
}
