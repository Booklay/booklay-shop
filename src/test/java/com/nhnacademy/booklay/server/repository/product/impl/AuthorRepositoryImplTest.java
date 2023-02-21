package com.nhnacademy.booklay.server.repository.product.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class AuthorRepositoryImplTest {
  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  TestEntityManager entityManager;

  EntityManager em;
  Author author;

  @BeforeEach
  void setUp(){
    em = entityManager.getEntityManager();
    author = DummyCart.getDummyAuthor();
  }
  @Test
  void findAllBy() {
    Pageable pageable = Pageable.ofSize(10);

    authorRepository.findAllByPageable(pageable);
  }

  @Test
  void findAuthorById() {
    Long authorNo = 1L;

    authorRepository.findById(authorNo);
  }

  @Test
  void findAllByPageable() {
    //given
    em.clear();

    authorRepository.save(author);
    Pageable pageable = PageRequest.of(0,20);

    //when
    Page<RetrieveAuthorResponse> authorPage = authorRepository.findAllByPageable(pageable);

    //then
    assertThat(authorPage).hasSize(1);
    assertThat(authorPage.getContent().get(0).getAuthorNo()).isEqualTo(author.getAuthorId());
  }
}