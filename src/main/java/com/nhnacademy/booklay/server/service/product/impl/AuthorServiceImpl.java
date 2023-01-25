package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberForAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductTag;
import com.nhnacademy.booklay.server.entity.ProductTag.Pk;
import com.nhnacademy.booklay.server.entity.Tag;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;
  private final MemberRepository memberRepository;
  private final ProductAuthorRepository productAuthorRepository;

  @Override
  public void createAuthor(CreateAuthorRequest request) {
    Author author = Author.builder()
        .name(request.getName())
        .build();

    if (Objects.nonNull(request.getMemberNo())) {
      Member member = memberRepository.findByMemberNo(request.getMemberNo())
          .orElseThrow(() -> new NotFoundException(Member.class, "Member not found"));

      author.setMember(member);
    }

    authorRepository.save(author);
  }

  @Override
  public void updateAuthor(UpdateAuthorRequest request) {
    authorRepository.findById(request.getId());

    Author author = Author.builder()
        .name(request.getName())
        .build();

    if (Objects.nonNull(request.getMemberNo())) {
      Member member = memberRepository.findByMemberNo(request.getMemberNo())
          .orElseThrow(() -> new NotFoundException(Member.class, "Member not found"));

      author.setMember(member);
    }

    authorRepository.save(author);
  }

  @Override
  public void deleteAuthor(DeleteIdRequest request) {
    Long id = request.getId();
    if(!authorRepository.existsById(id)){
      throw new NotFoundException(Author.class,"Delete target Author not found");
    }
    if(productAuthorRepository.existsByPk_AuthorId(id)){
      productAuthorRepository.deleteByPk_AuthorId(id);
    }
    authorRepository.deleteById(id);
  }

  @Override
  @Transactional
  public Page<RetrieveAuthorResponse> retrieveAllAuthor(Pageable pageable) {
    Page<Author> authors = authorRepository.findAllBy(pageable, Author.class);
    List<Author> content = authors.getContent();

    List<RetrieveAuthorResponse> contents = new ArrayList<>();

    for (Author author : content) {
      RetrieveAuthorResponse element = new RetrieveAuthorResponse(author.getAuthorId(), author.getName());

      if(Objects.nonNull(author.getMember())) {
        Member member = author.getMember();
        MemberForAuthorResponse memberDto = new MemberForAuthorResponse(member.getMemberNo(),
            member.getMemberId());

        element.setMember(memberDto);
      }

      contents.add(element);
    }

    return new PageImpl<>(contents, authors.getPageable(),
        authors.getTotalElements());
  }
}
