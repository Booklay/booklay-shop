package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import com.nhnacademy.booklay.server.repository.member.MemberGradeRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * author 양승아
 */
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
        Member saveMember = memberRepository.save(memberGrade.getMember());
        ReflectionTestUtils.setField(memberGrade, "id", 1L);
        ReflectionTestUtils.setField(memberGrade, "member", saveMember);

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
        Member saveMember = memberRepository.save(memberGrade.getMember());
        ReflectionTestUtils.setField(memberGrade, "id", 2L);
        ReflectionTestUtils.setField(memberGrade, "member", saveMember);
        memberGradeRepository.save(memberGrade);

        //when
        MemberGrade expected = memberGradeRepository.findById(memberGrade.getId()).orElseThrow(() -> new IllegalArgumentException("Authority not found"));

        //then
        assertThat(expected.getName()).isEqualTo(memberGrade.getName());
    }

    @Test
    @DisplayName("MemberGrade Entity JPA Auditing 기능 테스트")
    void testMemberGradeCreatedDated() {
        //given
        MemberGrade memberGrade = Dummy.getDummyMemberGrade();
        entityManager.persist(memberGrade.getMember().getGender());

        Member saveMember = memberRepository.save(memberGrade.getMember());
        ReflectionTestUtils.setField(memberGrade, "id", 3L);
        ReflectionTestUtils.setField(memberGrade, "member", saveMember);

        //when
        MemberGrade actual = memberGradeRepository.save(memberGrade);

        //then
        assertThat(actual.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }
}