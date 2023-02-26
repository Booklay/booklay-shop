package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.repository.member.BlockedMemberDetailRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.kafka.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author 양승아
 */
@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BlockedMemberDetailRepository blockedMemberDetailRepository;

    @BeforeEach
    public void setUp() {
        this.memberRepository.deleteAll();
        this.entityManager
                .getEntityManager()
                .createNativeQuery("ALTER TABLE member ALTER COLUMN `member_no` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("MemberRepository save 테스트")
    void testMemberSave() {
        //given
        Member member = Dummy.getDummyMember();
        entityManager.persist(member.getGender());

        //when
        Member expected = memberRepository.save(member);

        //then
        assertThat(expected.getName()).isEqualTo(member.getName());

    }

    @Test
    @DisplayName("MemberRepository findById 테스트")
    void testMemberFindById() {
        //given
        Member member = Dummy.getDummyMember();
        entityManager.persist(member.getGender());
        memberRepository.save(member);

        //when
        Member expected = memberRepository.findById(member.getMemberNo())
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        //then
        assertThat(expected.getMemberNo()).isEqualTo(member.getMemberNo());
    }

    @Test
    @DisplayName("MemberRepository findByMemberNo 테스트")
    void testMemberFindByMemberNo() {
        //given
        Member member = Dummy.getDummyMember();
        entityManager.persist(member.getGender());
        memberRepository.save(member);

        //when
        Member expected = memberRepository.findByMemberNo(member.getMemberNo())
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        //then
        assertThat(expected.getMemberNo()).isEqualTo(member.getMemberNo());
    }

    @Test
    @DisplayName("MemberRepository retrieveAll 테스트")
    void retrieveAll_successTest() {
        //given
        Member member1 = Dummy.getDummyMember();
        Member member2 = Dummy.getDummyMember();
        Member member3 = Dummy.getDummyMember();
        Member member4 = Dummy.getDummyMember();

        ReflectionTestUtils.setField(member2, "memberNo", 2L);
        ReflectionTestUtils.setField(member3, "memberNo", 3L);
        ReflectionTestUtils.setField(member4, "memberNo", 4L);

        entityManager.persist(member1.getGender());

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        //when
        PageRequest page = PageRequest.of(0, 3);
        Page<MemberRetrieveResponse> result = memberRepository.retrieveAll(page);

        //then
        assertThat(result.getSize()).isEqualTo(3 );
    }

    @Test
    @DisplayName("MemberRepository existByMemberId() 테스트")
    void testExistByMemberId() {
        //given
        Member member = Dummy.getDummyMember();
        entityManager.persist(member.getGender());
        memberRepository.save(member);

        //when
        boolean expected = memberRepository.existsByMemberId(member.getMemberId());

        //then
        assertThat(expected).isEqualTo(true);
    }

    @Test
    @DisplayName("MemberRepository retrieveValidMemberCount() 성공 테스트")
    void testRetrieveValidMemberCount() throws Exception {
        //given
        Member member = Dummy.getDummyMember();
        TestUtils.setFieldValue(member, "isBlocked", false);
        TestUtils.setFieldValue(member, "deletedAt", null);
        entityManager.persist(member.getGender());
        memberRepository.save(member);

        //when
        Long expected = memberRepository.retrieveValidMemberCount();

        //then
        assertThat(expected).isEqualTo(1);
    }

    @Test
    @DisplayName("MemberRepository retrieveBlockedMemberCount() 성공 테스트")
    void testRetrieveBlockedMemberCount() throws Exception {
        //given
        Member member = Dummy.getDummyMember();
        TestUtils.setFieldValue(member, "isBlocked", true);
        entityManager.persist(member.getGender());
        memberRepository.save(member);

        //when
        Long expected = memberRepository.retrieveBlockedMemberCount();

        //then
        assertThat(expected).isEqualTo(1);
    }

    @Test
    @DisplayName("MemberRepository retrieveDroppedMemberCount() 성공 테스트")
    void testRetrieveDroppedMemberCount() throws Exception {
        //given
        Member member = Dummy.getDummyMember();
        TestUtils.setFieldValue(member, "deletedAt", LocalDateTime.now());
        entityManager.persist(member.getGender());
        memberRepository.save(member);

        //when
        Long expected = memberRepository.retrieveDroppedMemberCount();

        //then
        assertThat(expected).isEqualTo(1);
    }


    @Test
    @DisplayName("Member Entity JPA Auditing 기능 테스트")
    void testMemberCreatedDated() {
        //given
        Member member = Dummy.getDummyMember();
        entityManager.persist(member.getGender());

        //when
        Member actual = memberRepository.save(member);

        //then
        assertThat(actual.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .isEqualTo(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    }

}