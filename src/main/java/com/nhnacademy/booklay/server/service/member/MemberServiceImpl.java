package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.MemberDto;
import com.nhnacademy.booklay.server.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    @Override
    @Transactional(readOnly = true)
    public MemberDto getMember(Long memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDto> getMembers() {
        return memberRepository.findAllBy();
    }
}
