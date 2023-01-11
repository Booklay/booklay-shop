package com.nhnacademy.booklay.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.service.product.impl.AuthorServiceImpl;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthorServiceTest {

  @InjectMocks
  private AuthorServiceImpl authorService;

  @Mock
  private AuthorRepository authorRepository;

  Author author;

  @BeforeEach
  void setup() {
    //given
    author = Author.builder()
        .name("최작가")
        .build();
  }

  @Test
  void testAuthorCreate_Success() {

    //when
    when(authorRepository.save(author)).thenReturn(author);

    Author expect = authorService.createAuthor(author);

    log.info("출력 : " + expect.getName()+ "  " + expect.getAuthorNo());

    //then
    assertThat(expect.getName()).isEqualTo(author.getName());
  }

  @Test
  void testAuthorRetrieve_Success() throws Exception {
    //given
    author.setAuthorNo(1L);
    when(authorRepository.findById(author.getAuthorNo())).thenReturn(Optional.ofNullable(author));

    //when
    Author expect = authorService.retrieveAuthorById(author.getAuthorNo());

    assertThat(expect.getAuthorNo()).isEqualTo(author.getAuthorNo());
  }

  @Test
  void testAuthorRetreieve_Failure() throws Exception {
    Long id = 3L;

//    assertThat(authorService.retrieveAuthorById(id)).
  }
}