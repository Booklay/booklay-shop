package com.nhnacademy.booklay.server.controller.member;

import com.nhnacademy.booklay.server.dto.member.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.service.member.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 양승아
 */
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberNo}")
    @ResponseStatus(HttpStatus.OK)
    public MemberRetrieveResponse retrieveMember(@PathVariable Long memberNo) {
        return memberService.retrieveMember(memberNo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createMember(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {
        memberService.createMember(memberCreateRequest);
    }


}
