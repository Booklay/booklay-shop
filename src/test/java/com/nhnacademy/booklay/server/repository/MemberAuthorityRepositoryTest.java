package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 양승아
 */
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
    void testMemberAuthoritySave() {
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
    void testMemberAuthorityFindById() {
        //given
        MemberAuthority memberAuthority = Dummy.getDummyMemberAuthority();
        entityManager.persist(memberAuthority.getAuthority());
        entityManager.persist(memberAuthority.getMember().getGender());
        memberRepository.save(memberAuthority.getMember());
        memberAuthorityRepository.save(memberAuthority);

        //when
        MemberAuthority expected = memberAuthorityRepository.findById(memberAuthority.getPk()).orElseThrow(()
            -> new IllegalArgumentException("MemberAuthority not found"));

        //then
        assertThat(expected.getPk()).isEqualTo(memberAuthority.getPk());
    }

}