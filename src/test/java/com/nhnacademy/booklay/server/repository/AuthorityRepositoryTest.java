package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AuthorityRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    AuthorityRepository authorityRepository;

    @Test
    @DisplayName("AuthorityRepository save test")
    void testAuthoritySave() {
        //given
        Authority authority = Dummy.getDummyAuthority();

        //when
        Authority expected = authorityRepository.save(authority);

        //then
        assertThat(expected.getName()).isEqualTo(authority.getName());

    }

    @Test
    @DisplayName("authorityRepository findById 테스트")
    void testAuthorityFindById() {
        //given
        Authority authority = Dummy.getDummyAuthority();
        authorityRepository.save(authority);

        //when
        Authority expected = authorityRepository.findById(authority.getId()).orElseThrow(() -> new IllegalArgumentException("Authority not found"));

        //then
        assertThat(expected.getId()).isEqualTo(authority.getId());
    }
}