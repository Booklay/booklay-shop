package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.reponse.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.BlockedMemberDetail;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import com.nhnacademy.booklay.server.exception.member.AdminAndAuthorAuthorityCannotExistTogetherException;
import com.nhnacademy.booklay.server.exception.member.AlreadyExistAuthorityException;
import com.nhnacademy.booklay.server.exception.member.AuthorityNotFoundException;
import com.nhnacademy.booklay.server.exception.member.GenderNotFoundException;
import com.nhnacademy.booklay.server.exception.member.MemberAlreadyExistedException;
import com.nhnacademy.booklay.server.exception.member.MemberAuthorityCannotBeDeletedException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.AuthorityRepository;
import com.nhnacademy.booklay.server.repository.member.BlockedMemberDetailRepository;
import com.nhnacademy.booklay.server.repository.member.GenderRepository;
import com.nhnacademy.booklay.server.repository.member.MemberAuthorityRepository;
import com.nhnacademy.booklay.server.repository.member.MemberGradeRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 양승아
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberGradeRepository memberGradeRepository;
    private final GenderRepository genderRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final BlockedMemberDetailRepository blockedMemberDetailRepository;

    private final GetMemberService getMemberService;

    public void checkExistsMemberId(String memberId) {
        if (memberRepository.existsByMemberId(memberId)) {
            throw new MemberAlreadyExistedException(memberId);
        }
    }

    @Override
    public void createMember(MemberCreateRequest createDto) {
        checkExistsMemberId(createDto.getMemberId());

        Gender gender = genderRepository.findByName(createDto.getGender()).orElseThrow(
            () -> new GenderNotFoundException(createDto.getGender()));

        Member member = createDto.toEntity(gender);
        MemberGrade grade = new MemberGrade(member, "화이트");

        Authority authority = authorityRepository.findByName("ROLE_MEMBER").orElseThrow(
            () -> new AuthorityNotFoundException("ROLE_MEMBER"));

        MemberAuthority memberAuthority =
            new MemberAuthority(new MemberAuthority.Pk(member.getMemberNo(), authority.getId()),
                                member, authority);

        memberRepository.save(member);
        memberGradeRepository.save(grade);
        memberAuthorityRepository.save(memberAuthority);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberRetrieveResponse retrieveMember(Long memberNo) {
        Member member = getMemberService.getMemberNo(memberNo);

        return MemberRetrieveResponse.fromEntity(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberRetrieveResponse> retrieveMembers(Pageable pageable) {
        return memberRepository.retrieveAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlockedMemberRetrieveResponse> retrieveBlockedMember(Pageable pageable) {
        return memberRepository.retrieveBlockedMembers(pageable);
    }

    @Override
    public void updateMember(Long memberNo, MemberUpdateRequest updateDto) {
        Member member = getMemberService.getMemberNo(memberNo);

        Gender gender = genderRepository.findByName(updateDto.getName()).orElseThrow(
            () -> new GenderNotFoundException(updateDto.getName()));

        member.updateMember(updateDto, gender);

    }

    @Override
    public void deleteMember(Long memberNo) {
        Member member = getMemberService.getMemberNo(memberNo);
        member.deleteMember();
    }

    /**
     * 이미 존재하는 권한 설정 시 에러
     * admin과 author 권한 동시에 존재 시 에러
     *
     * @param memberNo
     * @param authorityName
     */
    @Override
    public void createMemberAuthority(Long memberNo, String authorityName) {
        Member member = getMemberService.getMemberNo(memberNo);

        Authority authority = authorityRepository.findByName(authorityName).orElseThrow(
            () -> new AuthorityNotFoundException(authorityName));

        MemberAuthority.Pk pk = new MemberAuthority.Pk(memberNo, authority.getId());
        if (!memberAuthorityRepository.existsById(pk)) {
            throw new AlreadyExistAuthorityException(authorityName);
        }

        Authority adminAuthority = authorityRepository.findByName("admin").orElseThrow(
            () -> new NotFoundException(Authority.class, "authority admin not found"));

        Authority authorAuthority = authorityRepository.findByName("author").orElseThrow(
            () -> new NotFoundException(Authority.class, "author not found"));

        MemberAuthority.Pk adminPk = new MemberAuthority.Pk(memberNo, adminAuthority.getId());
        MemberAuthority.Pk authorPk = new MemberAuthority.Pk(memberNo, authorAuthority.getId());

        if (!authorityName.equals("member") && (memberAuthorityRepository.existsById(adminPk) ||
            memberAuthorityRepository.existsById(authorPk))) {
            throw new AdminAndAuthorAuthorityCannotExistTogetherException();
        }

        MemberAuthority memberAuthority = new MemberAuthority(pk, member, authority);

        memberAuthorityRepository.save(memberAuthority);
    }

    @Override
    public Optional<MemberLoginResponse> retrieveMemberById(String memberId) {
        return memberRepository.retrieveMemberByUserId(memberId);
    }

    @Override
    public void deleteMemberAuthority(Long memberNo, String authorityName) {
        getMemberService.getMemberNo(memberNo);

        if (authorityName.equals("member")) {
            throw new MemberAuthorityCannotBeDeletedException();
        }

        Authority authority = authorityRepository.findByName(authorityName).orElseThrow(
            () -> new AuthorityNotFoundException(authorityName));

        MemberAuthority.Pk pk = new MemberAuthority.Pk(memberNo, authority.getId());
        MemberAuthority memberAuthority = memberAuthorityRepository.findById(pk).orElseThrow(
            () -> new NotFoundException(MemberAuthority.class, "member authority not found"));

        memberAuthorityRepository.delete(memberAuthority);
    }

    @Override
    public void createMemberGrade(Long memberNo, String gradeName) {
        Member member = getMemberService.getMemberNo(memberNo);

        MemberGrade memberGrade = new MemberGrade(member, gradeName);

        memberGradeRepository.save(memberGrade);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberGradeRetrieveResponse> retrieveMemberGrades(Long memberNo,
                                                                  Pageable pageable) {
        getMemberService.getMemberNo(memberNo);

        return memberGradeRepository.findByMember_MemberNo(pageable, memberNo);
    }

    @Override
    public void blockMember(Long memberNo, MemberBlockRequest request) {
        Member member = getMemberService.getMemberNo(memberNo);
        member.setIsBlocked(true);

        BlockedMemberDetail blockedMemberDetail = new BlockedMemberDetail(member, request.getReason());

        blockedMemberDetailRepository.save(blockedMemberDetail);
    }

}
