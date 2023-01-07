package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("MemberRepository save test ")
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
        Member expected = memberRepository.findById(member.getMemberId()).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        //then
        assertThat(expected.getMemberId()).isEqualTo(member.getMemberId());
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
                .isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    }

}