package com.nhnacademy.booklay.server.repository.product;

import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Author;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class AuthorRepositoryTest {
  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  TestEntityManager entityManager;

  @Test
  void testFindAllBy_Success(){
    Author author = Author.builder()
        .name("최작가")
        .build();

    authorRepository.save(author);

    Page<RetrieveAuthorResponse> page = authorRepository.findAllBy(PageRequest.of(0,20));
  }
}