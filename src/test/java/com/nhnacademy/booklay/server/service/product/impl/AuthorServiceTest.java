package com.nhnacademy.booklay.server.service.product.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

  @InjectMocks
  private AuthorServiceImpl authorService;

  @Mock
  private AuthorRepository authorRepository;

  @Mock
  private MemberRepository memberRepository;

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
    if(Objects.nonNull(requestWithoutMember.getMemberNo())){
      given(memberRepository.findById(requestWithoutMember.getMemberNo())).willReturn(Optional.ofNullable(member));
      author.setMember(member);
    }

    authorService.createAuthor(requestWithoutMember);

    BDDMockito.then(authorRepository).should().save(any());
  }

  @Test
  void testAuthorCreateWithMember_success() {
    if(Objects.nonNull(requestWithMember.getMemberNo())){
      given(memberRepository.findById(requestWithMember.getMemberNo())).willReturn(Optional.ofNullable(member));
      author.setMember(member);
    }

    authorService.createAuthor(requestWithMember);

    BDDMockito.then(authorRepository).should().save(any());
  }

  @Test
  void testAuthorUpdateWithoutMember_success(){
    UpdateAuthorRequest request = new UpdateAuthorRequest(1L, "최규태");

    given(authorRepository.findById(request.getId())).willReturn(any());
    if(Objects.nonNull(request.getMemberNo())){
      given(memberRepository.findById(request.getMemberNo())).willReturn(Optional.ofNullable(member));
      author.setMember(member);
    }

    authorService.updateAuthor(request);

    BDDMockito.then(authorRepository).should().save(any());
  }

  @Test
  void testAuthorUpdateWithMember_success(){
    UpdateAuthorRequest request = new UpdateAuthorRequest(1L, "최규태");
    request.setMemberNo(1L);

    given(authorRepository.findById(request.getId())).willReturn(Optional.ofNullable(author));
    if(Objects.nonNull(request.getMemberNo())){
      given(memberRepository.findById(request.getMemberNo())).willReturn(Optional.ofNullable(member));
      author.setMember(member);
    }

    authorService.updateAuthor(request);

    BDDMockito.then(authorRepository).should().save(any());
  }


  @Test
  void testAuthorRetrieveAll_success() {
    //given
    given(authorRepository.findAllBy(any())).willReturn(Page.empty());

    //when
    Page<RetrieveAuthorResponse> pageResponse = authorService.retrieveAllAuthor(
        PageRequest.of(0, 10));

    //then
    BDDMockito.then(authorRepository).should().findAllBy(any());

    assertThat(pageResponse.getTotalElements()).isZero();
  }

  @Test
  void testAuthorRetrieveAllWithData_success() {
    //given
    given(authorRepository.findAllBy(any())).willReturn(Page.empty());
//    given(authorRepository.)
    //when
    Page<RetrieveAuthorResponse> pageResponse = authorService.retrieveAllAuthor(
        PageRequest.of(0, 10));

    //then
    BDDMockito.then(authorRepository).should().findAllBy(any());

    assertThat(pageResponse.getTotalElements()).isZero();
  }

  @Test
  void testAuthorDelete_success() {
    ReflectionTestUtils.setField(author, "authorId", 1L);

    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(author.getAuthorId());

    given(authorRepository.findById(author.getAuthorId())).willReturn(Optional.of(author));

    authorService.deleteAuthor(deleteIdRequest);

    then(authorRepository).should().delete(author);
  }

  @Test
  void testAuthorDelete_failure() {
    ReflectionTestUtils.setField(author, "authorId", 1L);
    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(author.getAuthorId());

    assertThatThrownBy(() -> authorService.deleteAuthor(deleteIdRequest)).isInstanceOf(
        NotFoundException.class);
  }
}