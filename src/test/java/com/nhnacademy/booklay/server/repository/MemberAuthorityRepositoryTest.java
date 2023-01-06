package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
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
class MemberAuthorityRepositoryTest {

    @Autowired
    TestEntityManager entityManager;


    @Autowired
    MemberAuthorityRepository memberAuthorityRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("MemberAuthorityRepository save test")
    void testMemberSave() {
        //given
        MemberAuthority memberAuthority = Dummy.getDummyMemberAuthority();
        entityManager.persist(memberAuthority.getAuthority());
        entityManager.persist(memberAuthority.getMember().getGender());
        memberRepository.save(memberAuthority.getMember());

        //when
        MemberAuthority expected = memberAuthorityRepository.save(memberAuthority);

        //then
        assertThat(expected).isNotNull();

    }

    @Test
    @DisplayName("MemberAuthorityRepository findById 테스트")
    void testMemberFindById() {
        //given
        MemberAuthority memberAuthority = Dummy.getDummyMemberAuthority();
        entityManager.persist(memberAuthority.getAuthority());
        entityManager.persist(memberAuthority.getMember().getGender());
        memberRepository.save(memberAuthority.getMember());

        //when
        MemberAuthority expected = memberAuthorityRepository.findById(memberAuthority.getPk()).orElseThrow(()
            -> new IllegalArgumentException("MemberAuthority not found"));

        //then
        assertThat(expected.getPk()).isEqualTo(memberAuthority.getPk());
    }

//    @Test
//    @DisplayName("Member Entity JPA Auditing 기능 테스트")
//    void testMemberCreatedDated() {
//        //given
//        Member member = Dummy.getDummyMember();
//        entityManager.persist(member.getGender());
//
//        //when
//        Member actual = memberRepository.save(member);
//
//        //then
//        assertThat(actual.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//
//    }

}