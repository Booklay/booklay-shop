package com.nhnacademy.booklay.server.service.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author 양승아
 */
@ExtendWith(MockitoExtension.class)
class GetMemberServiceTest {
    @InjectMocks
    GetMemberService getMemberService;

    @Mock
    MemberRepository memberRepository;
    Member member;

    @BeforeEach
    void setUp() {
        member = Dummy.getDummyMember();
    }

    @Test
    @DisplayName("회원번호로 조회 성공 테스트")
    void getMemberNoSuccessTest() {
        //given
        given(memberRepository.findByMemberNo(any())).willReturn(Optional.of(member));

        //when
        getMemberService.getMemberNo(member.getMemberNo());

        //then
        BDDMockito.then(memberRepository).should().findByMemberNo(member.getMemberNo());
    }

    @Test
    @DisplayName("회원아이디로 조회 성공 테스트")
    void getMemberIdSuccessTest() {
        //given
        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));

        //when
        getMemberService.getMemberId(member.getMemberId());

        //then
        BDDMockito.then(memberRepository).should().findByMemberId(member.getMemberId());
    }

    @Test
    @DisplayName("회원번호로 유효한 회원 조회 성공 테스트")
    void getValidMemberByMemberNoSuccessTest() {
        //given
        given(memberRepository.retrieveValidMemberByMemberNo(any())).willReturn(
            Optional.of(member));

        //when
        getMemberService.getValidMemberByMemberNo(member.getMemberNo());

        //then
        BDDMockito.then(memberRepository).should()
            .retrieveValidMemberByMemberNo(member.getMemberNo());
    }

    @Test
    @DisplayName("회원아이디로 유효한 회원 조회 성공 테스트")
    void getValidMemberByMemberIdSuccessTest() {
        //given
        given(memberRepository.retrieveValidMemberByMemberId(any())).willReturn(
            Optional.of(member));

        //when
        getMemberService.getValidMemberByMemberId(member.getMemberId());

        //then
        BDDMockito.then(memberRepository).should()
            .retrieveValidMemberByMemberId(member.getMemberId());
    }
}