package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Test
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

}