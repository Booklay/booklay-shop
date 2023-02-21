package com.nhnacademy.booklay.server.service.product.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import java.util.Objects;
import java.util.Optional;
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
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

  @InjectMocks
  private AuthorServiceImpl authorService;

  @Mock
  private AuthorRepository authorRepository;

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private ProductAuthorRepository productAuthorRepository;

  Author author;
  CreateAuthorRequest requestWithoutMember;
  CreateAuthorRequest requestWithMember;
  Member member;

  @BeforeEach
  void setup() {
    //given
    member = Dummy.getDummyMember();

    requestWithoutMember = new CreateAuthorRequest("최규태");
    requestWithMember = new CreateAuthorRequest("북레이");
    requestWithMember.setMemberNo(member.getMemberNo());

    author = Author.builder()
        .name(requestWithoutMember.getName())
        .build();
  }

  @Test
  void testAuthorCreate_Success() {
    if (Objects.nonNull(requestWithoutMember.getMemberNo())) {
      given(memberRepository.findById(requestWithoutMember.getMemberNo())).willReturn(
          Optional.ofNullable(member));
      author.setMember(member);
    }

    ReflectionTestUtils.setField(author,"authorId", 1L);

    authorService.createAuthor(requestWithoutMember);

    BDDMockito.then(authorRepository).should().save(any());
  }

  @Test
  void testAuthorCreateWithMember_success() {
    if (Objects.nonNull(requestWithMember.getMemberNo())) {
      given(memberRepository.findById(requestWithMember.getMemberNo())).willReturn(
          Optional.ofNullable(member));
      author.setMember(member);
    }

    authorService.createAuthor(requestWithMember);

    BDDMockito.then(authorRepository).should().save(any());
  }

  @Test
  void testAuthorUpdateWithoutMember_success() {
    UpdateAuthorRequest request = new UpdateAuthorRequest(1L, "최규태");

    given(authorRepository.findById(request.getId())).willReturn(Optional.ofNullable(author));
    if (Objects.nonNull(request.getMemberNo())) {
      given(memberRepository.findById(request.getMemberNo())).willReturn(
          Optional.ofNullable(member));
      author.setMember(member);
    }

    authorService.updateAuthor(request);

    BDDMockito.then(authorRepository).should().save(any());
  }

  @Test
  void testAuthorUpdateWithMember_success() {
    UpdateAuthorRequest request = new UpdateAuthorRequest(1L, "최규태");
    request.setMemberNo(1L);

    given(authorRepository.findById(request.getId())).willReturn(Optional.ofNullable(author));
    if (Objects.nonNull(request.getMemberNo())) {
      given(memberRepository.findById(request.getMemberNo())).willReturn(
          Optional.ofNullable(member));
      author.setMember(member);
    }

    authorService.updateAuthor(request);

    BDDMockito.then(authorRepository).should().save(any());
  }


  @Test
  void testAuthorRetrieveAll_success() {
    //given
    given(authorRepository.findAllByPageable(any())).willReturn(Page.empty());

    //when
    Page<RetrieveAuthorResponse> pageResponse = authorService.retrieveAllAuthor(
        PageRequest.of(0, 10));

    //then
    BDDMockito.then(authorRepository).should().findAllByPageable(any());

    assertThat(pageResponse.getTotalElements()).isZero();
  }

  @Test
  void testAuthorRetrieveAllWithData_success() {
    //given
    given(authorRepository.findAllByPageable(any())).willReturn(Page.empty());
//    given(authorRepository.)
    //when
    Page<RetrieveAuthorResponse> pageResponse = authorService.retrieveAllAuthor(
        PageRequest.of(0, 10));

    //then
    BDDMockito.then(authorRepository).should().findAllByPageable(any());

    assertThat(pageResponse.getTotalElements()).isZero();
  }

  @Test
  void testAuthorDelete_success() {
    //given
    ReflectionTestUtils.setField(author, "authorId", 1L);
    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(author.getAuthorId());

    //when
    authorService.deleteAuthor(deleteIdRequest);

    //then
    BDDMockito.then(productAuthorRepository).should().deleteByPk_AuthorId(deleteIdRequest.getId());
    BDDMockito.then(authorRepository).should().deleteById(deleteIdRequest.getId());
  }

  @Test
  void testAuthorRetrieveForUpdate_success() {
    Long authorId = 1L;
    //given
    ReflectionTestUtils.setField(author, "authorId", authorId);
    given(authorRepository.findById(authorId)).willReturn(Optional.ofNullable(author));

    //when
    authorService.retrieveAuthorForUpdate(authorId);

    //then
    BDDMockito.then(authorRepository).should().findById(authorId);
  }
}
