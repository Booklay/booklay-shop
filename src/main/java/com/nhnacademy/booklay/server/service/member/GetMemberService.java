package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetMemberService {

    private final MemberRepository memberRepository;

    public Member getMemberNo(Long memberNo) {
        return memberRepository.findByMemberNo(memberNo)
            .orElseThrow(() -> new MemberNotFoundException(memberNo));
    }

    public Member getMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
