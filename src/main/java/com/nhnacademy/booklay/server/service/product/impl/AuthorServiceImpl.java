package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

  @Override
  @Transactional
  public Author createAuthor(Author author) {
    return authorRepository.save(author);
  }


  @Override
  @Transactional
  public Author retrieveAuthorById(Long id) throws Exception {
    return authorRepository.findById(id).orElseThrow(()-> new NotFoundException(Author.class, "author not found"));
  }

  @Override
  @Transactional
  public Page<RetrieveAuthorResponse> retrieveAllAuthor(Pageable pageable) {
    return authorRepository.findAllBy(pageable, RetrieveAuthorResponse.class);
  }
}
