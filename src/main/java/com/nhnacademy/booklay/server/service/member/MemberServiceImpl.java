package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.MemberDto;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    @Override
    public MemberDto getMember(long memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }
}
