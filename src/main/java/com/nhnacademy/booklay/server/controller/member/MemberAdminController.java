package com.nhnacademy.booklay.server.controller.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * author 양승아
 */
@Slf4j
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MemberAdminController {
    private final MemberService memberService;

    @GetMapping
    public List<MemberRetrieveResponse> retrieveMembers(){
        //TODO 2: 1~10까지만 나옴. 수정필요.
        PageRequest page = PageRequest.of(1, 10);
        return memberService.retrieveMembers(page);
    }

    @GetMapping("/{memberNo}")
    public MemberRetrieveResponse retrieveMember(@PathVariable Long memberNo) {
        return memberService.retrieveMember(memberNo);
    }

}
