package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.member.reponse.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.BlockedMemberDetail;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.repository.member.BlockedMemberDetailRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author 양승아
 */
@DataJpaTest
@ActiveProfiles("test")
public class BlockedMemberDetailRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BlockedMemberDetailRepository blockedMemberDetailRepository;

    BlockedMemberDetail blockedMemberDetail;

    void clearRepo(String entityName, JpaRepository jpaRepository) {
        jpaRepository.deleteAll();

        String query =
            String.format("ALTER TABLE `%s` ALTER COLUMN `%s_no` RESTART WITH 1", entityName,
                entityName);

        this.entityManager
            .getEntityManager()
            .createNativeQuery(query)
            .executeUpdate();
    }

    @BeforeEach
    void setUp() {
        clearRepo("member", memberRepository);
        clearRepo("blocked_member_detail", blockedMemberDetailRepository);

        //given
        blockedMemberDetail = Dummy.getDummyBlockedMemberDetail();

        entityManager.persist(blockedMemberDetail.getMember().getGender());
        memberRepository.save(blockedMemberDetail.getMember());
    }

    @Test
    @DisplayName("DeliveryDetail save test")
    void save_successTest() {
        //given

        //when
        BlockedMemberDetail expected = blockedMemberDetailRepository.save(blockedMemberDetail);

        //then
        assertThat(expected.getReason()).isEqualTo(blockedMemberDetail.getReason());
    }

    @Test
    @DisplayName("DeliveryDetail findById test")
    void findById_successTest() {
        //given
        Member member = Dummy.getDummyMember();

        ReflectionTestUtils.setField(member, "isBlocked", true);

        memberRepository.save(member);
        blockedMemberDetailRepository.save(blockedMemberDetail);

        //when
        BlockedMemberDetail expected = blockedMemberDetailRepository.findById(blockedMemberDetail.getId())
            .orElseThrow(() -> new IllegalArgumentException());

        //then
        assertThat(expected.getId()).isEqualTo(blockedMemberDetail.getId());
    }

    @Test
    @DisplayName("DeliveryDetail findFirstByMember_MemberNoOrderByBlockedAtDesc success test ")
    void findFirstByMember_MemberNoOrderByBlockedAtDesc_successTest() {
        //given
        Member member = Dummy.getDummyMember();

        ReflectionTestUtils.setField(member, "isBlocked", true);

        BlockedMemberDetail blockedMemberDetail1 = BlockedMemberDetail.builder()
            .member(member)
            .reason("test reason1")
            .build();
        BlockedMemberDetail blockedMemberDetail2 = BlockedMemberDetail.builder()
            .member(member)
            .reason("test reason2")
            .build();

        memberRepository.save(member);
        blockedMemberDetailRepository.save(blockedMemberDetail1);
        blockedMemberDetailRepository.save(blockedMemberDetail2);

        //when
        BlockedMemberDetail expected = blockedMemberDetailRepository.findFirstByMember_MemberNoOrderByBlockedAtDesc(member.getMemberNo())
            .orElseThrow(() -> new IllegalArgumentException());

        //then
        assertThat(expected.getReason()).isEqualTo(blockedMemberDetail2.getReason());
    }

    @Test
    @DisplayName("DeliveryDetail findFirstByMember_MemberNoOrderByBlockedAtDesc fail test ")
    void findFirstByMember_MemberNoOrderByBlockedAtDesc_failTest() {
        //given
        Member member = Dummy.getDummyMember();

        ReflectionTestUtils.setField(member, "isBlocked", true);

        BlockedMemberDetail blockedMemberDetail1 = BlockedMemberDetail.builder()
            .member(member)
            .reason("test reason1")
            .build();
        BlockedMemberDetail blockedMemberDetail2 = BlockedMemberDetail.builder()
            .member(member)
            .reason("test reason2")
            .build();

        memberRepository.save(member);
        blockedMemberDetailRepository.save(blockedMemberDetail1);
        blockedMemberDetailRepository.save(blockedMemberDetail2);

        //when
        BlockedMemberDetail expected = blockedMemberDetailRepository.findFirstByMember_MemberNoOrderByBlockedAtDesc(member.getMemberNo())
            .orElseThrow(() -> new IllegalArgumentException());

        //then
        assertThat(expected.getReason()).isNotEqualTo(blockedMemberDetail1.getReason());
    }

    @Test
    @DisplayName("retrieveBlockedMembers 테스트")
    void retrieveBlockedMembers_successTest() {
        //given
        Member member1 = Dummy.getDummyMember();
        Member member2 = Dummy.getDummyMember();
        Member member3 = Dummy.getDummyMember();
        Member member4 = Dummy.getDummyMember();

        ReflectionTestUtils.setField(member2, "memberNo", 2L);
        ReflectionTestUtils.setField(member3, "memberNo", 3L);
        ReflectionTestUtils.setField(member4, "memberNo", 4L);

        ReflectionTestUtils.setField(member1, "isBlocked", true);
        ReflectionTestUtils.setField(member2, "isBlocked", true);
        ReflectionTestUtils.setField(member3, "isBlocked", true);
        ReflectionTestUtils.setField(member4, "isBlocked", true);

        BlockedMemberDetail blockedMemberDetail1 = BlockedMemberDetail.builder()
            .member(member1)
            .reason("test reason1")
            .build();
        BlockedMemberDetail blockedMemberDetail2 = BlockedMemberDetail.builder()
            .member(member2)
            .reason("test reason2")
            .build();
        BlockedMemberDetail blockedMemberDetail3 = BlockedMemberDetail.builder()
            .member(member3)
            .reason("test reason3")
            .build();
        BlockedMemberDetail blockedMemberDetail4 = BlockedMemberDetail.builder()
            .member(member4)
            .reason("test reason4")
            .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        blockedMemberDetailRepository.save(blockedMemberDetail1);
        blockedMemberDetailRepository.save(blockedMemberDetail2);
        blockedMemberDetailRepository.save(blockedMemberDetail3);
        blockedMemberDetailRepository.save(blockedMemberDetail4);

        //when
        PageRequest page = PageRequest.of(0, 3);
        Page<BlockedMemberRetrieveResponse> result = blockedMemberDetailRepository.retrieveBlockedMembers(page);

        //then
        assertThat(result.getSize()).isEqualTo(3);
    }
}
