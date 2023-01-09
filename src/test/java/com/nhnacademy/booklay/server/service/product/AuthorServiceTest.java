package com.nhnacademy.booklay.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.entity.Author;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class AuthorServiceTest {

  @Autowired
  AuthorService authorService;

  Author author;

  @BeforeEach
  void setup() {
    author = Author.builder()
        .name("최작가")
        .build();
  }

  @Test
  void testAuthorCreate_Success() {
    Author expect = authorService.createAuthor(author);

    assertThat(expect.getName()).isEqualTo(author.getName());
  }

  @Test
  void testAuthorRetrieve_Success() throws Exception {
    Long id  = authorService.createAuthor(author).getAuthorNo();

    Author expect = authorService.retrieveAuthorById(id);

    assertThat(expect.getAuthorNo()).isEqualTo(id);
  }

  @Test
  void testAuthorRetreieve_Failure() throws Exception {
    Long id  = 3L;

//    assertThat(authorService.retrieveAuthorById(id)).
  }
}