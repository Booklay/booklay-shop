package com.nhnacademy.booklay.server.controller.member;

import com.nhnacademy.booklay.server.dto.member.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * author 양승아
 */
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MemberAdminController {
    private final MemberService memberService;

    @GetMapping
    public List<MemberRetrieveResponse> retrieveMembers(){
        PageRequest page = PageRequest.of(1, 10);
        return memberService.getMembers(page);
    }

    @GetMapping("/{memberId}")
    public MemberRetrieveResponse retrieveMember(@PathVariable Long memberId) {
        return memberService.getMember(memberId);
    }

}
