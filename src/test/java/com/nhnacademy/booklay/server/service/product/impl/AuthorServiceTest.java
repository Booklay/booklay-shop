package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.service.product.impl.AuthorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthorServiceTest {

  @InjectMocks
  private AuthorServiceImpl authorService;

  @Mock
  private AuthorRepository authorRepository;

  Author author;
  CreateAuthorRequest request;

  @BeforeEach
  void setup() {
    //given
    author = Author.builder()
        .name("최규태")
        .build();

    request = new CreateAuthorRequest("최규태");
  }

  @Test
  void testAuthorCreate_Success() {
    assertDoesNotThrow(() -> authorService.createAuthor(request));
  }


  @Test
  void testAuthorRetrieveAll_success() {
    //given
    given(authorRepository.findAllBy(any(), any())).willReturn(Page.empty());

    //when
    Page<RetrieveAuthorResponse> pageResponse = authorService.retrieveAllAuthor(PageRequest.of(0, 10));

    //then
    BDDMockito.then(authorRepository).should().findAllBy(any(), any());

    assertThat(pageResponse.getTotalElements()).isZero();
  }

  @Test
  void testAuthorDelete_success() {
    ReflectionTestUtils.setField(author, "authorNo", 1L);

    given(authorRepository.existsById(author.getAuthorNo())).willReturn(true);
    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(author.getAuthorNo());
    authorService.deleteAuthor(deleteIdRequest);

    assertThat(authorRepository.findById(author.getAuthorNo())).isEmpty();
  }

  @Test
  void testAuthorDelete_failure() {
    ReflectionTestUtils.setField(author, "authorNo", 1L);
    given(authorRepository.existsById(author.getAuthorNo())).willReturn(false);

    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(author.getAuthorNo());

    assertThatThrownBy(() -> authorService.deleteAuthor(deleteIdRequest)).isInstanceOf(
        NotFoundException.class);
  }
}