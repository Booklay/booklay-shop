package com.nhnacademy.booklay.server.dto.member.reponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MemberLoginResponse {

    private final String userId;

    private final String password;

    private final List<String> authorities;
}
