package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.GenderNotFoundException;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.repository.member.GenderRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author 양승아
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final GenderRepository genderRepository;

    @Override
    public void createMember(MemberCreateRequest createDto) {
        Gender gender = genderRepository.findByName(createDto.getGender())
                .orElseThrow(() -> new GenderNotFoundException(createDto.getGender()));

        memberRepository.save(createDto.toEntity(gender));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberRetrieveResponse retrieveMember(Long memberNo) {
        Member member = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new MemberNotFoundException(memberNo));

        return MemberRetrieveResponse.fromEntity(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberRetrieveResponse> retrieveMembers(Pageable pageable) {
        return memberRepository.findAllBy(pageable);
    }

    @Override
    public void updateMember(Long memberNo, MemberUpdateRequest updateDto) {

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new MemberNotFoundException(memberNo));
        Gender gender = genderRepository.findByName(updateDto.getName())
                .orElseThrow(() -> new GenderNotFoundException(updateDto.getName()));

        member.updateMember(updateDto, gender);

    }

    @Override
    public void deleteMember(Long memberNo) {
        Member member = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new MemberNotFoundException(memberNo));
        member.setDeletedAt(LocalDateTime.now());
    }

}
