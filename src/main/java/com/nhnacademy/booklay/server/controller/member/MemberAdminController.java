package com.nhnacademy.booklay.server.controller.member;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<PageResponse<MemberRetrieveResponse>> retrieveMembers(Pageable pageable) {
        Page<MemberRetrieveResponse> responsePage = memberService.retrieveMembers(pageable);

        PageResponse<MemberRetrieveResponse> memberPageResponse
            = new PageResponse<>(responsePage.getNumber(), responsePage.getSize(),
            responsePage.getTotalPages(), responsePage.getContent());

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberPageResponse);
    }

    @GetMapping("/{memberNo}")
    public MemberRetrieveResponse retrieveMember(@PathVariable Long memberNo) {
        return memberService.retrieveMember(memberNo);
    }

    //TODO 4: DTO로 받을 것인지?
    @PostMapping("/authority/{memberNo}/{authorityName}")
    public ResponseEntity<Void> updateMemberAuthority(@PathVariable Long memberNo,
                                                      @PathVariable String authorityName) {
        memberService.createMemberAuthority(memberNo, authorityName);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @DeleteMapping("/authority/{memberNo}/{authorityName}")
    public ResponseEntity<Void> deleteMemberAuthority(@PathVariable Long memberNo,
                                                      @PathVariable String authorityName) {
        memberService.deleteMemberAuthority(memberNo, authorityName);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    @GetMapping("/grade/{memberNo}")
    public ResponseEntity<PageResponse<MemberGradeRetrieveResponse>> retrieveMemberGrades(
        @PathVariable Long memberNo, Pageable pageable) {
        Page<MemberGradeRetrieveResponse> responsePage =
            memberService.retrieveMemberGrades(memberNo, pageable);

        PageResponse<MemberGradeRetrieveResponse> memberPageResponse
            = new PageResponse<>(responsePage.getNumber(), responsePage.getSize(),
            responsePage.getTotalPages(), responsePage.getContent());

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberPageResponse);
    }

    @PostMapping("/grade/{memberNo}/{gradeName}")
    public ResponseEntity<Void> createMemberGrade(@PathVariable Long memberNo,
                                                  @PathVariable String gradeName) {
        memberService.createMemberGrade(memberNo, gradeName);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }
}
