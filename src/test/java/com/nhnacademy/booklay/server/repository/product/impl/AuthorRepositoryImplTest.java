package com.nhnacademy.booklay.server.repository.product.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class AuthorRepositoryImplTest {
  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  TestEntityManager entityManager;

  @Test
  void findAllBy() {
    Pageable pageable = Pageable.ofSize(10);

    authorRepository.findAllBy(pageable);
  }

  @Test
  void findAuthorById() {
    Long authorNo = 1L;

    authorRepository.findById(authorNo);
  }
}