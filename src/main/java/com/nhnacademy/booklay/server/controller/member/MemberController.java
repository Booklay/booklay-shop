package com.nhnacademy.booklay.server.controller.member;

import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.exception.category.ValidationFailedException;
import com.nhnacademy.booklay.server.service.member.MemberService;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 양승아
 */
@Slf4j
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
    public void createMember(@Valid @RequestBody MemberCreateRequest memberCreateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        memberService.createMember(memberCreateRequest);
    }

    @PutMapping("/{memberNo}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateMember(@PathVariable Long memberNo, @Valid @RequestBody MemberUpdateRequest memberUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        memberService.updateMember(memberNo, memberUpdateRequest);
    }

    @DeleteMapping("/{memberNo}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteMember(@PathVariable Long memberNo) {
        memberService.deleteMember(memberNo);
    }
}


