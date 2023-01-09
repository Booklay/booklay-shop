package com.nhnacademy.booklay.server.controller.member;

import com.nhnacademy.booklay.server.dto.member.MemberDto;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public List<MemberDto> retrieveMembers(){
        return memberService.getMembers();
    }

    @GetMapping("/{memberId}")
    public MemberDto retrieveMember(@PathVariable Long memberId) {
        return memberService.getMember(memberId);
    }
}
