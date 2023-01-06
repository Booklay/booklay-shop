package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class MemberGradeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberGradeRepository memberGradeRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("MemberGradeRepository save test ")
    void testMemberGradeSave() {
        //given
        MemberGrade memberGrade = Dummy.getDummyMemberGrade();
        entityManager.persist(memberGrade.getMember().getGender());
        memberRepository.save(memberGrade.getMember());

        //when
        MemberGrade expected = memberGradeRepository.save(memberGrade);

        //then
        assertThat(expected.getName()).isEqualTo(memberGrade.getName());

    }

    @Test
    @DisplayName("MemberGradeRepository findById 테스트")
    void testMemberGradeFindById() {
        //given
        MemberGrade memberGrade = Dummy.getDummyMemberGrade();
        entityManager.persist(memberGrade.getMember().getGender());
        memberRepository.save(memberGrade.getMember());
        memberGradeRepository.save(memberGrade);

        //when
        MemberGrade expected = memberGradeRepository.findById(memberGrade.getId()).orElseThrow(() -> new IllegalArgumentException("Authority not found"));

        //then
        assertThat(expected.getId()).isEqualTo(memberGrade.getId());
    }

    @Test
    @DisplayName("MemberGrade Entity JPA Auditing 기능 테스트")
    void testMemberGradeCreatedDated() {
        //given
        MemberGrade memberGrade = Dummy.getDummyMemberGrade();
        entityManager.persist(memberGrade.getMember().getGender());
        memberRepository.save(memberGrade.getMember());

        //when
        MemberGrade actual = memberGradeRepository.save(memberGrade);

        //then
        assertThat(actual.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }
}