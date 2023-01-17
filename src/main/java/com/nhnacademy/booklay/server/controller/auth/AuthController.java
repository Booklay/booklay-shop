package com.nhnacademy.booklay.server.controller.auth;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/members/login")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberLoginResponse> doLogin(@RequestParam String memberId) {

        MemberLoginResponse memberLoginResponse = memberService.retrieveMemberById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        return ResponseEntity.status(HttpStatus.OK)
                            .body(memberLoginResponse);
    }

}