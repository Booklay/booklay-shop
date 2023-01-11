package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.repository.member.GenderRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    MemberServiceImpl memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    GenderRepository genderRepository;

    MemberCreateRequest memberCreateRequest;
    Member member;
    Gender gender;

    private static final Long VALID_MEMBER_NO = 1L;
    private static final Long INVALID_MEMBER_NO = 2L;

    @BeforeEach
    void setUp() {
        memberCreateRequest = Dummy.getDummyMemberCreateRequest();
        gender = Gender.builder()
                .id(1L)
                .name("M")
                .build();
        member = memberCreateRequest.toEntity(gender);
    }


    @Test
    @DisplayName("멤버 생성 테스트")
    void testCreateMember() {
        //given
        given(genderRepository.findByName(anyString())).willReturn(Optional.of(gender));
        given(memberRepository.save(any(Member.class))).willReturn(member);

        //when
        memberService.createMember(memberCreateRequest);

        //then
        then(memberRepository).should().save(any(Member.class));
    }

    @Test
    @DisplayName("멤버 조회 테스트")
    void testRetrieveMember() {
        //given
        given(memberRepository.findByMemberNo(anyLong())).willReturn(Optional.of(member));

        //when
        memberService.retrieveMember(VALID_MEMBER_NO);

        //then
        then(memberRepository).should().findByMemberNo(VALID_MEMBER_NO);
    }

    @Test
    @DisplayName("잘못된 번호로 멤버 조회시 MemberNotFoundException 발생 테스트")
    void testRetrieveMember_whenRetrieveWithInvalidMemberNo() {
        //given
        given(memberRepository.findByMemberNo(anyLong())).willThrow(new MemberNotFoundException(INVALID_MEMBER_NO));

        //when, then
        assertThatThrownBy(() -> memberService.retrieveMember(INVALID_MEMBER_NO))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("Member Not Found, MemberNo : 2");

    }

    @Test
    void retrieveMembers() {
    }

    @Test
    void updateMember() {
    }

    @Test
    void deleteMember() {
    }
}