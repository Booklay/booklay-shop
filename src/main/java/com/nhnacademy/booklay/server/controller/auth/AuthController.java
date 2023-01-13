package com.nhnacademy.booklay.server.controller.auth;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/members")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    public ResponseEntity<MemberLoginResponse> doLogin(@RequestParam String memberId) {

        return null;
    }
}
