package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import com.nhnacademy.booklay.server.exception.member.GenderNotFoundException;
import com.nhnacademy.booklay.server.exception.member.MemberAlreadyExistedException;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.repository.AuthorityRepository;
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
 *
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

    private final GetMemberService getMemberService;

    public void checkExistsMemberId(String memberId) {
        if (memberRepository.existsByMemberId(memberId)) {
            throw new MemberAlreadyExistedException(memberId);
        }
    }

    @Override
    public void createMember(MemberCreateRequest createDto) {
        checkExistsMemberId(createDto.getMemberId());

        Gender gender = genderRepository.findByName(createDto.getGender())
            .orElseThrow(() -> new GenderNotFoundException(createDto.getGender()));

        Member member = createDto.toEntity(gender);
        MemberGrade grade = new MemberGrade(member, "화이트");

        //TODO 3: Make Custom Exception?
        Authority authority = authorityRepository.findByName("ROLE_MEMBER")
            .orElseThrow(() -> new IllegalArgumentException());

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
        Member member = getMember(memberNo);

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
        Member member = getMember(memberNo);
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
        Member member = getMember(memberNo);

        //TODO 3: Make Custom Exception?
        Authority authority = authorityRepository.findByName(authorityName)
            .orElseThrow(() -> new IllegalArgumentException());


        MemberAuthority.Pk pk = new MemberAuthority.Pk(memberNo, authority.getId());
        if (!memberAuthorityRepository.existsById(pk)) {
            //TODO 4: custom exception 만들기(이미 있는 권한)
            throw new IllegalArgumentException();
        }

        Authority adminAuthority = authorityRepository.findByName("admin").get();
        Authority authorAuthority = authorityRepository.findByName("author").get();
        MemberAuthority.Pk adminPk = new MemberAuthority.Pk(memberNo, adminAuthority.getId());
        MemberAuthority.Pk authorPk = new MemberAuthority.Pk(memberNo, authorAuthority.getId());

        //TODO 5: custom exception 만들기(admin과 author권한 동시 존재 불가)
        if (!authorityName.equals("member") &&
            (memberAuthorityRepository.existsById(adminPk) ||
                memberAuthorityRepository.existsById(authorPk))) {
            throw new IllegalArgumentException();
        }

        MemberAuthority memberAuthority =
            new MemberAuthority(pk, member, authority);

        memberAuthorityRepository.save(memberAuthority);
    }

    @Override
    public Optional<MemberLoginResponse> retrieveMemberById(String memberId) {
        return memberRepository.retrieveMemberByUserId(memberId);
    }

    @Override
    public void deleteMemberAuthority(Long memberNo, String authorityName) {
        memberRepository.findByMemberNo(memberNo)
            .orElseThrow(() -> new MemberNotFoundException(memberNo));

        //TODO 3: custom exception (member authority는 삭제할 수 없음)
        if (authorityName.equals("member")) {
            throw new IllegalArgumentException();
        }

        //TODO 3: Make Custom Exception?
        Authority authority = authorityRepository.findByName(authorityName)
            .orElseThrow(() -> new IllegalArgumentException());

        MemberAuthority.Pk pk = new MemberAuthority.Pk(memberNo, authority.getId());
        //TODO 3: Custom Exception (없는 권한은 삭제할 수 없음)
        MemberAuthority memberAuthority = memberAuthorityRepository.findById(pk)
            .orElseThrow(() -> new IllegalArgumentException());


        memberAuthorityRepository.deleteById(pk);
    }

    @Override
    public void createMemberGrade(Long memberNo, String gradeName) {
        Member member = getMember(memberNo);

        MemberGrade memberGrade = new MemberGrade(member, gradeName);

        memberGradeRepository.save(memberGrade);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberGradeRetrieveResponse> retrieveMemberGrades(Long memberNo,
                                                                  Pageable pageable) {
        memberRepository.findByMemberNo(memberNo)
            .orElseThrow(() -> new MemberNotFoundException(memberNo));

        return memberGradeRepository.findByMember_MemberNo(pageable, memberNo);
    }

    private Member getMember(Long memberNo) {
        return memberRepository.findByMemberNo(memberNo)
            .orElseThrow(() -> new MemberNotFoundException(memberNo));
    }

}
