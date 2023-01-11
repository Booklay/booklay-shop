package com.nhnacademy.booklay.server.controller.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.service.member.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 양승아
 */
@Slf4j
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MemberAdminController {
    private final MemberService memberService;

    @GetMapping
    public Page<MemberRetrieveResponse> retrieveMembers(Pageable pageable){

        return memberService.retrieveMembers(pageable);
    }

    @GetMapping("/{memberNo}")
    public MemberRetrieveResponse retrieveMember(@PathVariable Long memberNo) {
        return memberService.retrieveMember(memberNo);
    }

}
