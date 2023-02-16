package com.nhnacademy.booklay.server.controller.member;

import com.nhnacademy.booklay.server.dto.ErrorResponse;
import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberMainRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.exception.member.AdminAndAuthorAuthorityCannotExistTogetherException;
import com.nhnacademy.booklay.server.exception.member.AlreadyExistAuthorityException;
import com.nhnacademy.booklay.server.exception.member.AuthorityNotFoundException;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.service.member.MemberService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 양승아
 */
@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private static final String MEMBER_NOT_FOUND_ERROR_CODE = "MemberNotFound";
    private static final String AUTHORITY_NOT_FOUND_ERROR_CODE = "AuthorityNotFound";
    private static final String ADMIN_AND_AUTHOR_AUTHORITY_CANNOT_EXIST_TOGETHER_ERROR_CODE =
        "AdminAndAuthorAuthorityCannotExistTogether";
    private static final String ALREADY_EXIST_AUTHORITY_ERROR_CODE = "AlreadyExistAuthority";

    private final MemberService memberService;

    /**
     * 회원가입 시 아이디 중복체크 메소드
     * @param memberId
     * @return
     */
    @GetMapping("/exist/{memberId}")
    public ResponseEntity<Boolean> existMemberId(@PathVariable String memberId){
        boolean result = memberService.checkMemberId(memberId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(result);
    }
    /**
     * 회원가입 시 닉네임 중복체크 메소드
     * @param nickName
     * @return
     */
    @GetMapping("/exist/nickName/{nickName}")
    public ResponseEntity<Boolean> existNickName(@PathVariable String nickName){
        boolean result = memberService.checkNickName(nickName);
        return ResponseEntity.status(HttpStatus.OK)
            .body(result);
    }
    /**
     * 회원가입 시 이메일 중복체크 메소드
     * @param eMail
     * @return
     */
    @GetMapping("/exist/eMail/{eMail}")
    public ResponseEntity<Boolean> existEMail(@PathVariable String eMail){
        boolean result = memberService.checkEMail(eMail);
        return ResponseEntity.status(HttpStatus.OK)
            .body(result);
    }

    @GetMapping("/{memberNo}")
    public ResponseEntity<MemberRetrieveResponse> retrieveMember(@PathVariable Long memberNo) {

        MemberRetrieveResponse memberResponse = memberService.retrieveMember(memberNo);
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberResponse);
    }

    @GetMapping("/main/{memberNo}")
    public ResponseEntity<MemberMainRetrieveResponse> retrieveMemberMain(
        @PathVariable Long memberNo) {

        MemberMainRetrieveResponse memberMainResponse = memberService.retrieveMemberMain(memberNo);
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberMainResponse);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MemberLoginResponse> retrieveMemberByEmail(
        @PathVariable String email) {

        MemberLoginResponse memberResponse =
            memberService.retrieveMemberByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                    MemberNotFoundException.class,
                    MEMBER_NOT_FOUND_ERROR_CODE));

        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(memberResponse);

    }

    @GetMapping("/memberinfo/{email}")
    public ResponseEntity<MemberRetrieveResponse> retrieveMemberInfoByEmail(@PathVariable String email) {

        MemberRetrieveResponse memberRetrieveResponse = memberService.retrieveMemberInfoByEmail(email)
                                                                     .orElseThrow(
                                                                         () -> new NotFoundException(
                                                                             MemberNotFoundException.class,
                                                                             MEMBER_NOT_FOUND_ERROR_CODE));

        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(memberRetrieveResponse);
    }

    @GetMapping("/grade/{memberNo}")
    public ResponseEntity<PageResponse<MemberGradeRetrieveResponse>> retrieveMemberGrade(
        @PathVariable Long memberNo,
        Pageable pageable) {
        Page<MemberGradeRetrieveResponse> responsePage =
            memberService.retrieveMemberGrades(memberNo, pageable);

        PageResponse<MemberGradeRetrieveResponse> memberPageResponse
            = new PageResponse<>(responsePage);

        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(memberPageResponse);
    }

    @GetMapping("/authority/{memberNo}")
    public ResponseEntity<List<MemberAuthorityRetrieveResponse>> retrieveMemberAuthority(
        @PathVariable Long memberNo) {

        List<MemberAuthorityRetrieveResponse> memberResponse =
            memberService.retrieveMemberAuthority(memberNo);
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(memberResponse);
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> createMember(
        @Valid @RequestBody MemberCreateRequest memberCreateRequest) {

        Long memberNo = memberService.createMember(memberCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(Collections.singletonMap("memberNo", memberNo));
    }

    @PutMapping("/{memberNo}")
    public ResponseEntity<Void> updateMember(@PathVariable Long memberNo,
                                             @Valid @RequestBody
                                             MemberUpdateRequest memberUpdateRequest) {

        memberService.updateMember(memberNo, memberUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                             .build();
    }

    @DeleteMapping("/{memberNo}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberNo) {
        memberService.deleteMember(memberNo);
        return ResponseEntity.status(HttpStatus.OK)
                             .build();
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(MemberNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ErrorResponse.builder().code(MEMBER_NOT_FOUND_ERROR_CODE)
                                                .message(ex.getMessage())
                                                .build());
    }

    @ExceptionHandler(AuthorityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorityNotFoundException(
        AuthorityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ErrorResponse.builder().code(AUTHORITY_NOT_FOUND_ERROR_CODE)
                                                .message(ex.getMessage()).build());
    }

    @ExceptionHandler(AdminAndAuthorAuthorityCannotExistTogetherException.class)
    public ResponseEntity<ErrorResponse> handleAdminAndAuthorAuthorityCannotExistTogetherException(
        AdminAndAuthorAuthorityCannotExistTogetherException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ErrorResponse.builder()
                                                .code(
                                                    ADMIN_AND_AUTHOR_AUTHORITY_CANNOT_EXIST_TOGETHER_ERROR_CODE)
                                                .message(ex.getMessage()).build());
    }

    @ExceptionHandler(AlreadyExistAuthorityException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistAuthorityException(
        AlreadyExistAuthorityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ErrorResponse.builder().code(ALREADY_EXIST_AUTHORITY_ERROR_CODE)
                                                .message(ex.getMessage()).build());
    }
}


