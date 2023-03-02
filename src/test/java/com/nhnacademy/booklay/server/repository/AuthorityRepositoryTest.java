package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.repository.product.impl.AuthorRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class AuthorityRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    AuthorRepositoryImpl authorRepositoryImpl;

    @Test
    @DisplayName("AuthorityRepository save test")
    void testAuthoritySave() {
        //given
        Authority authority = Dummy.getDummyAuthorityAsAdmin();

        //when
        Authority expected = authorityRepository.save(authority);

        //then
        assertThat(expected.getName()).isEqualTo(authority.getName());

    }

    @Test
    @DisplayName("authorityRepository findById 테스트")
    void testAuthorityFindById() {
        //given
        Authority authority = Dummy.getDummyAuthorityAsAdmin();
        authorityRepository.save(authority);

        //when
        Authority expected = authorityRepository.findById(authority.getId()).orElseThrow(() -> new IllegalArgumentException("Authority not found"));

        //then
        assertThat(expected.getId()).isEqualTo(authority.getId());
    }

    @Test
    @DisplayName("authorityRepository findAllByPageable 테스트")
    void findAllByPageable() {
        //given
        PageRequest request = PageRequest.of(1, 1);

        //when
        authorRepositoryImpl.findAllByPageable(request);

        //then
    }
}