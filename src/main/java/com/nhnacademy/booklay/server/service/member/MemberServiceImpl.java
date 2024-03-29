package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.request.MemberAuthorityUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.response.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberChartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberMainRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.BlockedMemberDetail;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import com.nhnacademy.booklay.server.exception.member.AdminAndAuthorAuthorityCannotExistTogetherException;
import com.nhnacademy.booklay.server.exception.member.AlreadyBlockedMemberException;
import com.nhnacademy.booklay.server.exception.member.AlreadyExistAuthorityException;
import com.nhnacademy.booklay.server.exception.member.AlreadyUnblockedMemberException;
import com.nhnacademy.booklay.server.exception.member.AuthorityNotFoundException;
import com.nhnacademy.booklay.server.exception.member.BlockedMemberDetailNotFoundException;
import com.nhnacademy.booklay.server.exception.member.GenderNotFoundException;
import com.nhnacademy.booklay.server.exception.member.MemberAlreadyExistedException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.AuthorityRepository;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryDestinationRepository;
import com.nhnacademy.booklay.server.repository.member.BlockedMemberDetailRepository;
import com.nhnacademy.booklay.server.repository.member.GenderRepository;
import com.nhnacademy.booklay.server.repository.member.MemberAuthorityRepository;
import com.nhnacademy.booklay.server.repository.member.MemberGradeRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.mypage.PointHistoryRepository;
import com.nhnacademy.booklay.server.utils.Grade;
import java.time.LocalDateTime;
import java.util.List;
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
    private final DeliveryDestinationRepository deliveryDestinationRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final GetMemberService getMemberService;

    private void checkExistsMemberId(String memberId) {
        if (memberRepository.existsByMemberId(memberId)) {
            throw new MemberAlreadyExistedException(memberId);
        }
    }
    
    /**
     * 회원가입 시 회원 생성하는 메소드
     *
     * @param createDto
     */
    @Override
    public Long createMember(MemberCreateRequest createDto) {
        checkExistsMemberId(createDto.getMemberId());

        Gender gender = genderRepository.findByName(createDto.getGender()).orElseThrow(
            () -> new GenderNotFoundException(createDto.getGender()));

        Member member = createDto.toEntity(gender);

        Authority authority = authorityRepository.findByName("ROLE_USER").orElseThrow(
            () -> new AuthorityNotFoundException("ROLE_USER"));

        MemberAuthority memberAuthority =
            new MemberAuthority(new MemberAuthority.Pk(member.getMemberNo(), authority.getId()),
                member, authority);

        Member savedMember = memberRepository.save(member);
        memberGradeRepository.save(member.addGrade(Grade.WHITE.getKorGrade()));
        memberAuthorityRepository.save(memberAuthority);

        return savedMember.getMemberNo();
    }

    @Override
    public void createMemberGrade(Long memberNo, String gradeName) {
        Member member = getMemberService.getMemberNo(memberNo);

        MemberGrade memberGrade = new MemberGrade(member, gradeName);

        memberGradeRepository.save(memberGrade);
    }

    /**
     * 회원 권한 만드는 메소드
     * 이미 존재하는 권한 설정 시 에러
     * admin과 author 권한 동시에 존재 시 에러
     *
     * @param memberNo
     * @param request
     */
    @Override
    public void createMemberAuthority(Long memberNo, MemberAuthorityUpdateRequest request) {
        Member member = getMemberService.getMemberNo(memberNo);

        Authority authority =
            authorityRepository.findByName(request.getAuthorityName()).orElseThrow(
                () -> new AuthorityNotFoundException(request.getAuthorityName()));

        MemberAuthority.Pk pk = new MemberAuthority.Pk(memberNo, authority.getId());
        if (!memberAuthorityRepository.existsById(pk)) {
            throw new AlreadyExistAuthorityException(request.getAuthorityName());
        }

        Authority adminAuthority = authorityRepository.findByName("admin").orElseThrow(
            () -> new NotFoundException(Authority.class, "authority admin not found"));

        Authority authorAuthority = authorityRepository.findByName("author").orElseThrow(
            () -> new NotFoundException(Authority.class, "author not found"));

        MemberAuthority.Pk adminPk = new MemberAuthority.Pk(memberNo, adminAuthority.getId());
        MemberAuthority.Pk authorPk = new MemberAuthority.Pk(memberNo, authorAuthority.getId());

        if (!request.getAuthorityName().equals("member") &&
            (memberAuthorityRepository.existsById(adminPk) ||
                memberAuthorityRepository.existsById(authorPk))) {
            throw new AdminAndAuthorAuthorityCannotExistTogetherException();
        }

        MemberAuthority memberAuthority = new MemberAuthority(pk, member, authority);

        memberAuthorityRepository.save(memberAuthority);
    }

    /**
     * 회원 탈퇴 시 처리하는 메소드
     *
     * @param memberNo 탈퇴할 회원
     * @param request
     */
    @Override
    public void createBlockMember(Long memberNo, MemberBlockRequest request) {
        Member member = getMemberService.getMemberNo(memberNo);

        if (Boolean.TRUE.equals(member.getIsBlocked())) {
            throw new AlreadyBlockedMemberException(member);
        }

        member.setIsBlocked(true);

        BlockedMemberDetail blockedMemberDetail =
            new BlockedMemberDetail(member, request.getReason());

        blockedMemberDetailRepository.save(blockedMemberDetail);
    }

    /**
     * 특정 회원의 등급내역 보여주는 메소드
     *
     * @param memberNo
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MemberGradeRetrieveResponse> retrieveMemberGrades(Long memberNo,
                                                                  Pageable pageable) {
        getMemberService.getMemberNo(memberNo);

        return memberGradeRepository.findByMemberMemberNo(pageable, memberNo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberLoginResponse> retrieveMemberById(String memberId) {
        return memberRepository.retrieveMemberByUserId(memberId);
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
        return blockedMemberDetailRepository.retrieveBlockedMembers(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberLoginResponse> retrieveMemberByEmail(String email) {
        return memberRepository.retrieveMemberByEmail(email);
    }

    @Override
    public Optional<MemberRetrieveResponse> retrieveMemberInfoByEmail(String email) {
        return memberRepository.retrieveMemberInfoByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlockedMemberRetrieveResponse> retrieveBlockedMemberDetail(Long memberNo,
                                                                           Pageable pageable) {
        getMemberService.getMemberNo(memberNo);

        return blockedMemberDetailRepository.retrieveBlockedMemberDetail(memberNo, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DroppedMemberRetrieveResponse> retrieveDroppedMembers(Pageable pageable) {
        return memberRepository.retrieveDroppedMembers(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberAuthorityRetrieveResponse> retrieveMemberAuthority(Long memberNo) {
        return memberAuthorityRepository.retrieveAuthoritiesByMemberNo(memberNo);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberChartRetrieveResponse retrieveMemberChart() {
        return MemberChartRetrieveResponse.builder()
            .validMemberCount(memberRepository.retrieveValidMemberCount())
            .blockedMemberCount(memberRepository.retrieveBlockedMemberCount())
            .droppedMemberCount(memberRepository.retrieveDroppedMemberCount())
            .build();
    }

    /**
     * myPage main을 위한 메소드
     * 개인정보 마스킹처리
     *
     * @param memberNo 개인정보 주인 회원번호
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public MemberMainRetrieveResponse retrieveMemberMain(Long memberNo) {
        Member member = getMemberService.getValidMemberByMemberNo(memberNo);
        MemberGrade grade = memberGradeRepository.retrieveCurrentMemberGrade(memberNo)
            .orElseThrow();
        TotalPointRetrieveResponse point =
            pointHistoryRepository.retrieveLatestPointHistory(memberNo)
                .orElse(new TotalPointRetrieveResponse(null, 0));

        MemberMainRetrieveResponse response = MemberMainRetrieveResponse.builder()
            .memberNo(memberNo)
            .gender(member.getGender().getName())
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .name(member.getName())
            .birthday(member.getBirthday())
            .phoneNo(member.getPhoneNo())
            .email(member.getEmail())
            .memberGrade(grade.getName())
            .currentTotalPoint(point.getTotalPoint())
            .build();

        response.maskingMember();

        return response;
    }


    @Override
    public void blockMemberCancel(Long blockedMemberDetailId) {
        BlockedMemberDetail blockedMemberDetail =
            blockedMemberDetailRepository.findById(blockedMemberDetailId)
                .orElseThrow(() -> new BlockedMemberDetailNotFoundException(blockedMemberDetailId));

        if (blockedMemberDetail.getReleasedAt() != null) {
            throw new AlreadyUnblockedMemberException(blockedMemberDetail.getMember());
        }

        blockedMemberDetail.getMember().setIsBlocked(false);

        blockedMemberDetail.setReleasedAt(LocalDateTime.now());
    }


    @Override
    public void updateMember(Long memberNo, MemberUpdateRequest updateDto) {
        Member member = getMemberService.getMemberNo(memberNo);

        Gender gender = genderRepository.findByName(updateDto.getGender()).orElseThrow(
            () -> new GenderNotFoundException(updateDto.getGender()));

        memberRepository.save(member.updateMember(updateDto, gender));

    }

    @Override
    public void deleteMember(Long memberNo) {
        Member member = getMemberService.getMemberNo(memberNo);
        member.deleteMember();
        deliveryDestinationRepository.deleteAllByMember_MemberNo(memberNo);
        pointHistoryRepository.deleteAllByMember_MemberNo(memberNo);
    }


    @Override
    public void deleteMemberAuthority(Long memberNo, String authorityName) {
        getMemberService.getMemberNo(memberNo);

        Authority authority = authorityRepository.findByName(authorityName)
            .orElseThrow(() -> new AuthorityNotFoundException(authorityName));

        MemberAuthority.Pk pk = new MemberAuthority.Pk(memberNo, authority.getId());
        MemberAuthority memberAuthority = memberAuthorityRepository.findById(pk).orElseThrow(
            () -> new NotFoundException(MemberAuthority.class, "member authority not found"));

        memberAuthorityRepository.delete(memberAuthority);
    }

    @Override
    public boolean checkMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    @Override
    public boolean checkNickName(String nickName) {
        return memberRepository.existsByNickname(nickName);
    }

    @Override
    public boolean checkEMail(String eMail) {
        return memberRepository.existsByEmail(eMail);
    }

}
