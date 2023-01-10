package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

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
    @DisplayName("MemberRepository findAllBy 테스트")
    void testMemberFindAllBy() {
        //given
        Member member = Dummy.getDummyMember();
        entityManager.persist(member.getGender());
        memberRepository.save(member);

        //when
        PageRequest page = PageRequest.of(1, 10);
        Page<Member> members = memberRepository.findAllBy(page);

        //then
        assertThat(members).isNotNull();
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