package com.nhnacademy.booklay.server.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class OauthServiceTest {

    @InjectMocks
    OauthService oauthService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberService memberService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void checkUser() {
        // Given
        String testString = "test";
        when(memberRepository.existsByEmail(anyString())).thenReturn(false);

        // When
        oauthService.checkUser(testString, testString);

        // Then
        then(memberService).should().createMember(any());
    }

    @Test
    void checkUser_repositoryReturnTrue() {
        // Given
        String testString = "test";
        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

        // When
        oauthService.checkUser(testString, testString);

        // Then
        then(memberRepository).should().existsByEmail(any());
    }
}