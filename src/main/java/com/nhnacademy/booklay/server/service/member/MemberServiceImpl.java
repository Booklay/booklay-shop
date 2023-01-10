package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.repository.GenderRepository;
import com.nhnacademy.booklay.server.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * author 양승아
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final GenderRepository genderRepository;
    @Override
    @Transactional(readOnly = true)
    public MemberRetrieveResponse getMember(Long memberId) {
        Member member  = memberRepository.findByMemberId(memberId);
        return MemberRetrieveResponse.fromEntity(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberRetrieveResponse> getMembers(Pageable pageable) {
        return MemberRetrieveResponse.fromEntity(memberRepository.findAllBy(pageable));
    }

    @Override
    @Transactional
    public void createMember(MemberCreateRequest memberCreateRequest) {
        Optional<Gender> genderOptional = genderRepository.findByName(memberCreateRequest.getGender());
        if(genderOptional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        memberRepository.save(memberCreateRequest.toEntity(genderOptional.get()));
    }
}
