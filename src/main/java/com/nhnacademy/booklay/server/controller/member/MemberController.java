package com.nhnacademy.booklay.server.controller.member;

import com.nhnacademy.booklay.server.dto.ErrorResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.exception.member.AdminAndAuthorAuthorityCannotExistTogetherException;
import com.nhnacademy.booklay.server.exception.member.AlreadyExistAuthorityException;
import com.nhnacademy.booklay.server.exception.member.AuthorityNotFoundException;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.service.member.GetMemberService;
import com.nhnacademy.booklay.server.service.member.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private static final String ADMIN_AND_AUTHOR_AUTHORITY_CANNOT_EXIST_TOGETHER_ERROR_CODE = "AdminAndAuthorAuthorityCannotExistTogether";
    private static final String ALREADY_EXIST_AUTHORITY_ERROR_CODE = "MemberNotFound";


    private final MemberService memberService;
    @GetMapping("/{memberNo}")
    public ResponseEntity<MemberRetrieveResponse> retrieveMember(@PathVariable Long memberNo) {

        MemberRetrieveResponse memberResponse = memberService.retrieveMember(memberNo);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {

        memberService.createMember(memberCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
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
                .body(ErrorResponse.builder().code(MEMBER_NOT_FOUND_ERROR_CODE).message(ex.getMessage()).build());
    }

    @ExceptionHandler(AuthorityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorityNotFoundException(
        AuthorityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.builder().code(AUTHORITY_NOT_FOUND_ERROR_CODE).message(ex.getMessage()).build());
    }

    @ExceptionHandler(AdminAndAuthorAuthorityCannotExistTogetherException.class)
    public ResponseEntity<ErrorResponse> handleAdminAndAuthorAuthorityCannotExistTogetherException(
        AdminAndAuthorAuthorityCannotExistTogetherException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder().code(ADMIN_AND_AUTHOR_AUTHORITY_CANNOT_EXIST_TOGETHER_ERROR_CODE).message(ex.getMessage()).build());
    }

    @ExceptionHandler(AlreadyExistAuthorityException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistAuthorityException(
        AlreadyExistAuthorityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder().code(ALREADY_EXIST_AUTHORITY_ERROR_CODE).message(ex.getMessage()).build());
    }
}


