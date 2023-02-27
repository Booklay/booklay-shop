package com.nhnacademy.booklay.server.service;

import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.service.member.MemberService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public boolean checkUser(String id, String email) {

        if (memberRepository.existsByEmail(email)) {
            return true;
        }

        MemberCreateRequest memberCreateRequest =
            MemberCreateRequest.builder()
                .gender("F")
                .memberId("GIT_" + id)
                .password(passwordEncoder.encode(email))
                .nickname(email.split("@")[0])
                .name(email.split("@")[0])
                .birthday(LocalDate.now())
                .phoneNo("01000000000")
                .email(email)
                .build();

        memberService.createMember(memberCreateRequest);

        return false;
    }
}
