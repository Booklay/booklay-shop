package com.nhnacademy.booklay.server.controller.admin.member;

import com.nhnacademy.booklay.server.dto.ErrorResponse;
import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberAuthorityUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.server.dto.member.response.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberChartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeChartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.exception.member.AlreadyBlockedMemberException;
import com.nhnacademy.booklay.server.exception.member.AlreadyExistAuthorityException;
import com.nhnacademy.booklay.server.exception.member.AlreadyUnblockedMemberException;
import com.nhnacademy.booklay.server.service.member.MemberService;
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
import org.springframework.web.bind.annotation.RequestBody;
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
    private static final String ALREADY_UNBLOCKED_MEMBER_ERROR_CODE = "AlreadyUnblockedMember";
    private static final String ALREADY_BLOCKED_MEMBER_ERROR_CODE = "AlreadyBlockedMember";

    /**
     * 관리자의 회원 리스트 조회 메소드
     *
     * @param pageable
     * @return 회원리스트
     */
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

    /**
     * 관리자의 회원 한 명 조회 메소드
     *
     * @param memberNo 조회할 회원 번호
     * @return
     */
    @GetMapping("/{memberNo}")
    public MemberRetrieveResponse retrieveMember(@PathVariable Long memberNo) {
        return memberService.retrieveMember(memberNo);
    }

    /**
     * 관리자의 회원 권한 수정 메소드
     *
     * @param memberNo 수정할 회원 번호
     * @param request
     * @return
     */
    @PostMapping("/authority/{memberNo}")
    public ResponseEntity<Void> updateMemberAuthority(@PathVariable Long memberNo,
                                                      @Valid @RequestBody
                                                      MemberAuthorityUpdateRequest request) {
        memberService.createMemberAuthority(memberNo, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    /**
     * 관리자의 회원 권한 삭제 메소드
     *
     * @param memberNo      권한 삭제할 회원의 번호
     * @param authorityName
     * @return
     */
    @DeleteMapping("/authority/{memberNo}/{authorityName}")
    public ResponseEntity<Void> deleteMemberAuthority(@PathVariable Long memberNo,
                                                      @PathVariable String authorityName) {
        memberService.deleteMemberAuthority(memberNo, authorityName);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    /**
     * 관리자의 특정 회원 등급 이력 조회 메소드
     *
     * @param memberNo 조회할 회원의 번호
     * @param pageable
     * @return
     */
    @GetMapping("/grade/{memberNo}")
    public ResponseEntity<PageResponse<MemberGradeRetrieveResponse>> retrieveMemberGrades(
        @PathVariable Long memberNo, Pageable pageable) {
        Page<MemberGradeRetrieveResponse> responsePage =
            memberService.retrieveMemberGrades(memberNo, pageable);

        PageResponse<MemberGradeRetrieveResponse> memberPageResponse
            = new PageResponse<>(responsePage);

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberPageResponse);
    }

    /**
     * 관리자의 차단회원 리스트 조회 메소드
     *
     * @param pageable
     * @return
     */
    @GetMapping("/block")
    public ResponseEntity<PageResponse<BlockedMemberRetrieveResponse>> retrieveBlockedMembers(
        Pageable pageable) {
        Page<BlockedMemberRetrieveResponse> responsePage =
            memberService.retrieveBlockedMember(pageable);

        PageResponse<BlockedMemberRetrieveResponse> memberPageResponse
            = new PageResponse<>(responsePage);

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberPageResponse);
    }

    /**
     * 관리자의 특정 회원의 차단 이력 리스트 조회 메소드
     *
     * @param memberNo 조회할 회원 번호
     * @param pageable
     * @return
     */
    @GetMapping("/block/detail/{memberNo}")
    public ResponseEntity<PageResponse<BlockedMemberRetrieveResponse>> retrieveBlockedMemberDetail(
        @PathVariable Long memberNo,
        Pageable pageable) {
        Page<BlockedMemberRetrieveResponse> responsePage =
            memberService.retrieveBlockedMemberDetail(memberNo, pageable);

        PageResponse<BlockedMemberRetrieveResponse> memberPageResponse
            = new PageResponse<>(responsePage);

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberPageResponse);
    }

    /**
     * 관리자의 탈퇴회원 리스트 조회 메소드
     *
     * @param pageable
     * @return
     */
    @GetMapping("/dropped")
    public ResponseEntity<PageResponse<DroppedMemberRetrieveResponse>> retrieveDroppedMembers(
        Pageable pageable) {
        Page<DroppedMemberRetrieveResponse> responsePage =
            memberService.retrieveDroppedMembers(pageable);

        PageResponse<DroppedMemberRetrieveResponse> memberPageResponse
            = new PageResponse<>(responsePage);

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberPageResponse);
    }

    /**
     * 관리자의 회원 통계를 위한 데이터 조회 메소드
     *
     * @return
     */
    @GetMapping("/chart")
    public MemberChartRetrieveResponse retrieveMemberChart() {
        return memberService.retrieveMemberChart();
    }

    /**
     * 관리자의 회원 등급 통계를 위한 데이터 조회 메소드
     *
     * @return
     */
    @GetMapping("/grade/chart")
    public MemberGradeChartRetrieveResponse retrieveMemberGradeChart() {
        return memberService.retrieveMemberGradeChart();
    }

    /**
     * 관리자의 회원 등급 생성 메소드
     *
     * @param memberNo  등급 생성할 회원 번호
     * @param gradeName 등급 이름
     * @return
     */
    @PostMapping("/grade/{memberNo}/{gradeName}")
    public ResponseEntity<Void> createMemberGrade(@PathVariable Long memberNo,
                                                  @PathVariable String gradeName) {
        memberService.createMemberGrade(memberNo, gradeName);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    /**
     * 관리자의 회원 차단 메소드
     *
     * @param request  차단 상세 내용 데이터가 담긴 dto
     * @param memberNo 차단 당할 회원 번호
     * @return
     */
    @PostMapping("/block/{memberNo}")
    public ResponseEntity<Void> memberBlock(@Valid @RequestBody MemberBlockRequest request,
                                            @PathVariable Long memberNo) {
        memberService.createBlockMember(memberNo, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    /**
     * 관리자의 회원 차단 해제 메소드
     *
     * @param blockedMemberDetailId 차단을 해제할 회원의 번호
     * @return
     */
    @PostMapping("/block/cancel/{blockedMemberDetailId}")
    public ResponseEntity<Void> memberBlockCancel(@PathVariable Long blockedMemberDetailId) {
        memberService.blockMemberCancel(blockedMemberDetailId);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    @ExceptionHandler(AlreadyUnblockedMemberException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyUnblockedMemberException(
        AlreadyExistAuthorityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder().code(ALREADY_UNBLOCKED_MEMBER_ERROR_CODE)
                .message(ex.getMessage()).build());
    }

    @ExceptionHandler(AlreadyBlockedMemberException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyBlockedMemberException(
        AlreadyExistAuthorityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder().code(ALREADY_BLOCKED_MEMBER_ERROR_CODE)
                .message(ex.getMessage()).build());
    }
}

