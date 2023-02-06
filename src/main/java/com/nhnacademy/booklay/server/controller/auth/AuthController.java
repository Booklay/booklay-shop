package com.nhnacademy.booklay.server.controller.auth;

import com.nhnacademy.booklay.server.dto.ErrorResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/members/login")
@RequiredArgsConstructor
public class AuthController {

    private static final String MEMBER_NOT_FOUND_ERROR_CODE = "없는 회원이거나, 탈퇴한 회원입니다.";
    private static final String MEMBER_BLOCKED_ERROR = "Member blocked";

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberLoginResponse> doLogin(@RequestParam String memberId) {

        MemberLoginResponse memberLoginResponse = memberService.retrieveMemberById(memberId)
                                                               .orElseThrow(() ->
                                                                   new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR_CODE));

        if (memberLoginResponse.getIsBlocked()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                       .header("error-code", MEMBER_BLOCKED_ERROR)
                       .build();
        }

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

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(MemberNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ErrorResponse.builder().code(MEMBER_NOT_FOUND_ERROR_CODE)
                                                .message(ex.getMessage())
                                                .build());
    }

}
