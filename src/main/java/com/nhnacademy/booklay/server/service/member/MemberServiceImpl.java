package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.CreateMemberFailedException;
import com.nhnacademy.booklay.server.exception.member.GenderNotFoundException;
import com.nhnacademy.booklay.server.exception.member.MemberAlreadyExistedException;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.exception.member.UpdateMemberFailedException;
import com.nhnacademy.booklay.server.repository.GenderRepository;
import com.nhnacademy.booklay.server.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
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
    @Transactional(readOnly = true)
    public MemberRetrieveResponse retrieveMember(Long memberNo) {
        Member member = memberRepository.findByMemberNo(memberNo)
            .orElseThrow(() -> new MemberNotFoundException(memberNo));

        return MemberRetrieveResponse.fromEntity(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberRetrieveResponse> retrieveMembers(Pageable pageable) {
        return MemberRetrieveResponse.fromEntity(memberRepository.findAllBy(pageable));
    }

    @Override
    public void createMember(MemberCreateRequest createDto) {
        if (!memberRepository.existsByMemberId(createDto.getMemberId())) {
            try {
                Gender gender = genderRepository.findByName(createDto.getGender())
                    .orElseThrow(() -> new GenderNotFoundException(createDto.getGender()));
                memberRepository.save(createDto.toEntity(gender));
            } catch (GenderNotFoundException e) {
                throw new CreateMemberFailedException(createDto.getMemberId());
            }
        } else {
            throw new MemberAlreadyExistedException(createDto.getMemberId());
        }
    }

    @Override
    public void updateMember(Long memberNo, MemberUpdateRequest updateDto) {
        try {
            Gender gender = genderRepository.findByName(updateDto.getGender())
                .orElseThrow(() -> new GenderNotFoundException(updateDto.getGender()));
            Member member = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new MemberNotFoundException(memberNo));
            member.update(
                gender,
                updateDto.getPassword(),
                updateDto.getNickname(),
                updateDto.getName(),
                updateDto.getBirthday(),
                updateDto.getPhoneNo(),
                updateDto.getEmail(),
                member.getIsBlocked());
            memberRepository.save(member);
        } catch (Exception e) {
            throw new UpdateMemberFailedException(memberNo);
        }
    }

    @Override
    public void deleteMember(Long memberNo) {
            Member member = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new MemberNotFoundException(memberNo));
            member.setDeletedAt(LocalDateTime.now());

            memberRepository.save(member);
    }
}
