package com.nhnacademy.booklay.server.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import com.nhnacademy.booklay.server.exception.member.AuthorityNotFoundException;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.repository.AuthorityRepository;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryDestinationRepository;
import com.nhnacademy.booklay.server.repository.member.BlockedMemberDetailRepository;
import com.nhnacademy.booklay.server.repository.member.GenderRepository;
import com.nhnacademy.booklay.server.repository.member.MemberAuthorityRepository;
import com.nhnacademy.booklay.server.repository.member.MemberGradeRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.mypage.PointHistoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    MemberServiceImpl memberService;

    @Mock
    GetMemberService getMemberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberGradeRepository memberGradeRepository;

    @Mock
    MemberAuthorityRepository memberAuthorityRepository;

    @Mock
    AuthorityRepository authorityRepository;

    @Mock
    GenderRepository genderRepository;

    @Mock
    DeliveryDestinationRepository deliveryDestinationRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    BlockedMemberDetailRepository blockedMemberDetailRepository;

    MemberCreateRequest memberCreateRequest;
    MemberUpdateRequest memberUpdateRequest;
    MemberLoginResponse memberLoginResponse;
    MemberRetrieveResponse memberRetrieveResponse;
    MemberAuthorityRetrieveResponse memberAuthorityRetrieveResponse;
    Member member;
    Gender gender;
    Authority authority;
    MemberAuthority memberAuthority;

    private static final Long VALID_MEMBER_NO = 1L;
    private static final Long INVALID_MEMBER_NO = 2L;

    @BeforeEach
    void setUp() {
        memberCreateRequest = Dummy.getDummyMemberCreateRequest();
        memberUpdateRequest = Dummy.getDummyMemberUpdateRequest();
        gender = Gender.builder()
            .id(1L)
            .name("M")
            .build();
        member = memberCreateRequest.toEntity(gender);
        authority = Dummy.getDummyAuthorityAsMember();
        memberLoginResponse = Dummy.getDummyMemberLoginResponse();
        memberRetrieveResponse = Dummy.getDummyMemberRetrieveResponse();
        memberAuthorityRetrieveResponse = Dummy.getDummyMemberAuthorityRetrieveResponse();
        memberAuthority = Dummy.getDummyMemberAuthority();
    }


    @Test
    @DisplayName("멤버 생성 테스트")
    void testCreateMember() {
        //given
        given(genderRepository.findByName(anyString())).willReturn(Optional.of(gender));
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(authorityRepository.findByName(anyString())).willReturn(Optional.of(authority));

        //when
        memberService.createMember(memberCreateRequest);

        //then
        then(memberRepository).should().save(any(Member.class));
        then(memberGradeRepository).should().save(any(MemberGrade.class));
        then(memberAuthorityRepository).should().save(any(MemberAuthority.class));
    }

    @Test
    @DisplayName("멤버 조회 테스트")
    void testRetrieveMember() {
        //given
        given(getMemberService.getMemberNo(any())).willReturn(member);
        //when
        memberService.retrieveMember(VALID_MEMBER_NO);

        //then
        then(getMemberService).should().getMemberNo(VALID_MEMBER_NO);
    }

    @Test
    @DisplayName("잘못된 번호로 멤버 조회시 MemberNotFoundException 발생 테스트")
    void testRetrieveMember_whenRetrieveWithInvalidMemberNo() {
        //given
        given(getMemberService.getMemberNo(any())).willThrow(
            new MemberNotFoundException(INVALID_MEMBER_NO));

        //when, then
        assertThatThrownBy(() -> memberService.retrieveMember(INVALID_MEMBER_NO))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessageContaining("Member Not Found, MemberNo : 2");

    }

    @Test
    @DisplayName("관리자의 회원 전체조회시 Page 반환 성공 테스트")
    void retrieveMembersSuccessTest() {
        //given
        given(memberRepository.retrieveAll(any())).willReturn(Page.empty());

        //when
        Page<MemberRetrieveResponse> pageResponse =
            memberService.retrieveMembers(PageRequest.of(0, 10));

        //then
        then(memberRepository).should().retrieveAll(any());
        assertThat(pageResponse.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("멤버 등급 생성 성공 테스트")
    void CreateMemberGradeSuccessTest() {
        //given
        given(getMemberService.getMemberNo(any())).willReturn(member);

        //when
        memberService.createMemberGrade(member.getMemberNo(), "화이트");

        //then
        then(memberGradeRepository).should().save(any(MemberGrade.class));

    }

    @Test
    @DisplayName("멤버 등급 조회시 Page 반환 성공 테스트")
    void retrieveMemberGradesSuccessTest() {
        //given
        given(getMemberService.getMemberNo(any())).willReturn(member);
        given(memberGradeRepository.findByMember_MemberNo(any(), any())).willReturn(Page.empty());

        //when
        Page<MemberGradeRetrieveResponse> pageResponse =
            memberService.retrieveMemberGrades(any(), PageRequest.of(0, 10));

        //then
        then(getMemberService).should().getMemberNo(any());
        then(memberGradeRepository).should().findByMember_MemberNo(any(), any());
        assertThat(pageResponse.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("멤버 등급 조회시 Page 반환 성공 테스트")
    void retrieveBlockedMemberSuccessTest() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        given(blockedMemberDetailRepository.retrieveBlockedMembers(pageRequest)).willReturn(
            Page.empty());

        //when
        memberService.retrieveBlockedMember(pageRequest);

        //then
        BDDMockito.then(blockedMemberDetailRepository).should().retrieveBlockedMembers(pageRequest);
    }

    @Test
    @DisplayName("아이디로 회원 로그인 정보 반환 성공 테스트")
    void retrieveMemberByIdSuccessTest() {
        //given
        given(memberRepository.retrieveMemberByUserId(any())).willReturn(
            Optional.of(memberLoginResponse));

        //when
        memberService.retrieveMemberById(memberLoginResponse.getUserId());

        //then
        BDDMockito.then(memberRepository).should().retrieveMemberByUserId(any());
    }

    @Test
    @DisplayName("이메일로 회원 로그인 정보 반환 성공 테스트")
    void retrieveMemberByEmailSuccessTest() {
        //given
        given(memberRepository.retrieveMemberByEmail(any())).willReturn(
            Optional.of(memberLoginResponse));

        //when
        memberService.retrieveMemberByEmail(memberLoginResponse.getEmail());

        //then
        BDDMockito.then(memberRepository).should().retrieveMemberByEmail(any());
    }

    @Test
    @DisplayName("이메일로 회원 조회 성공 테스트")
    void retrieveMemberInfoByEmailSuccessTest() {
        //given
        given(memberRepository.retrieveMemberInfoByEmail(any())).willReturn(
            Optional.of(memberRetrieveResponse));

        //when
        memberService.retrieveMemberInfoByEmail(memberRetrieveResponse.getEmail());

        //then
        BDDMockito.then(memberRepository).should().retrieveMemberInfoByEmail(any());
    }

    @Test
    @DisplayName("차단회원 상세내역 조회 성공 테스트")
    void retrieveBlockedMemberDetailSuccessTest() {
        //given
        given(getMemberService.getMemberNo(any())).willReturn(member);
        PageRequest pageRequest = PageRequest.of(0, 10);
        given(blockedMemberDetailRepository.retrieveBlockedMemberDetail(member.getMemberNo(),
            pageRequest)).willReturn(Page.empty());

        //when
        memberService.retrieveBlockedMemberDetail(member.getMemberNo(), pageRequest);

        //then
        BDDMockito.then(blockedMemberDetailRepository).should()
            .retrieveBlockedMemberDetail(member.getMemberNo(), pageRequest);
    }

    @Test
    @DisplayName("탈퇴회원 상세내역 조회 성공 테스트")
    void retrieveDroppedMembersSuccessTest() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        given(memberRepository.retrieveDroppedMembers(pageRequest)).willReturn(Page.empty());

        //when
        memberService.retrieveDroppedMembers(pageRequest);

        //then
        BDDMockito.then(memberRepository).should().retrieveDroppedMembers(pageRequest);
    }

    @Test
    @DisplayName("이메일로 회원 조회 성공 테스트")
    void retrieveMemberAuthoritySuccessTest() {
        //given
        given(memberAuthorityRepository.retrieveAuthoritiesByMemberNo(
            member.getMemberNo())).willReturn(
            List.of(memberAuthorityRetrieveResponse));

        //when
        memberService.retrieveMemberAuthority(member.getMemberNo());

        //then
        BDDMockito.then(memberAuthorityRepository).should()
            .retrieveAuthoritiesByMemberNo(member.getMemberNo());
    }

    @Test
    @DisplayName("MemberChartRetrieveResponse 반환 성공 테스트")
    void retrieveMemberChartSuccessTest() {
        //given
        given(memberRepository.retrieveValidMemberCount()).willReturn(1L);
        given(memberRepository.retrieveBlockedMemberCount()).willReturn(1L);
        given(memberRepository.retrieveDroppedMemberCount()).willReturn(1L);

        //when
        memberService.retrieveMemberChart();

        //then
        BDDMockito.then(memberRepository).should().retrieveValidMemberCount();
        BDDMockito.then(memberRepository).should().retrieveBlockedMemberCount();
        BDDMockito.then(memberRepository).should().retrieveDroppedMemberCount();

    }

    @Test
    @DisplayName("회원 정보 수정 성공 테스트")
    void updateMemberSuccessTest() {
        //given
        given(getMemberService.getMemberNo(any())).willReturn(member);
        given(genderRepository.findByName(any())).willReturn(Optional.of(gender));

        //when
        memberService.updateMember(member.getMemberNo(), memberUpdateRequest);

        //then
        BDDMockito.then(memberRepository).should().save(any());
    }

    @Test
    @DisplayName("회원 정보 삭제 성공 테스트")
    void deleteMemberSuccessTest() {
        //given
        given(getMemberService.getMemberNo(any())).willReturn(member);

        //when
        memberService.deleteMember(member.getMemberNo());

        //then
        BDDMockito.then(deliveryDestinationRepository).should()
            .deleteAllByMember_MemberNo(member.getMemberNo());
        BDDMockito.then(pointHistoryRepository).should()
            .deleteAllByMember_MemberNo(member.getMemberNo());
    }

    @Test
    @DisplayName("권한 삭제 성공 테스트")
    void deleteMemberAuthoritySuccessTest() {
        //given
        given(getMemberService.getMemberNo(any())).willReturn(member);
        given(authorityRepository.findByName(any())).willReturn(Optional.of(authority));
        given(memberAuthorityRepository.findById(any())).willReturn(Optional.of(memberAuthority));

        //when
        memberService.deleteMemberAuthority(member.getMemberNo(), authority.getName());

        //then
        BDDMockito.then(memberAuthorityRepository).should().delete(memberAuthority);
    }

    @Test
    @DisplayName("권한 삭제 시 존재하지 않는 권한 이름 테스트")
    void testDeleteMemberAuthority_ifNotExistedAuthorityName_thenThrowsAuthorityNotFoundException() {
        when(authorityRepository.findByName(any())).thenThrow(
            new AuthorityNotFoundException(any()));

        assertThatThrownBy(
            () -> memberService.deleteMemberAuthority(member.getMemberNo(), authority.getName()))
            .isInstanceOf(AuthorityNotFoundException.class);
    }

    @Test
    @DisplayName("아이디로 회원 존재 여부 조회 성공 테스트")
    void checkMemberIdSuccessTest() {
        //given
        given(memberRepository.existsByMemberId(any())).willReturn(true);

        //when
        memberService.checkMemberId(member.getMemberId());

        //then
        BDDMockito.then(memberRepository).should().existsByMemberId(member.getMemberId());
    }

    @Test
    @DisplayName("아이디로 회원 존재 여부 조회 실패 테스트")
    void checkMemberIdFailTest() {
        //given
        given(memberRepository.existsByMemberId(any())).willReturn(false);

        //when
        memberService.checkMemberId(member.getMemberId());

        //then
        BDDMockito.then(memberRepository).should().existsByMemberId(member.getMemberId());
    }

    @Test
    @DisplayName("닉네임으로 회원 조회 성공 테스트")
    void checkNickNameSuccessTest() {
        //given
        given(memberRepository.existsByNickname(any())).willReturn(true);

        //when
        memberService.checkNickName(member.getNickname());

        //then
        BDDMockito.then(memberRepository).should().existsByNickname(member.getNickname());
    }

    @Test
    @DisplayName("닉네임으로 회원 조회 실패 테스트")
    void checkNickNameFailTest() {
        //given
        given(memberRepository.existsByNickname(any())).willReturn(false);

        //when
        memberService.checkNickName(member.getNickname());

        //then
        BDDMockito.then(memberRepository).should().existsByNickname(member.getNickname());
    }

    @Test
    @DisplayName("이메일로 회원 조회 성공 테스트")
    void checkEMailSuccessTest() {
        //given
        given(memberRepository.existsByEmail(any())).willReturn(true);

        //when
        memberService.checkEMail(member.getEmail());

        //then
        BDDMockito.then(memberRepository).should().existsByEmail(member.getEmail());

    }

    @Test
    @DisplayName("이메일로 회원 조회 실패 테스트")
    void checkEMailFailTest() {
        //given
        given(memberRepository.existsByEmail(any())).willReturn(false);

        //when
        memberService.checkEMail(member.getEmail());

        //then
        BDDMockito.then(memberRepository).should().existsByEmail(member.getEmail());
    }
}