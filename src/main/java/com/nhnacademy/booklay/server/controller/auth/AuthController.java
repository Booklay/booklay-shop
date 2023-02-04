package com.nhnacademy.booklay.server.controller.auth;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/members/login")
@RequiredArgsConstructor
public class AuthController {

    private static final String MEMBER_NOT_FOUND_ERROR_CODE = "MemberNotFound";

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberLoginResponse> doLogin(@RequestParam String memberId) {

        MemberLoginResponse memberLoginResponse = memberService.retrieveMemberById(memberId)
                                                               .orElseThrow(
                                                                   IllegalArgumentException::new);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(memberLoginResponse);
    }

    @GetMapping("/email")
    public ResponseEntity<MemberLoginResponse> retrieveMemberByEmail(
        @RequestParam String email) {

        MemberLoginResponse memberLoginResponse = memberService.retrieveMemberByEmail(email)
                                                               .orElseThrow(
                                                                   IllegalArgumentException::new);

        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(memberLoginResponse);

    }

}
