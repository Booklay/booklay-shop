package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
  private final AuthorRepository authorRepository;

  @Override
  public Author createAuthor(Author author) {
    return authorRepository.save(author);
  }


  @Override
  public Author retrieveAuthorById(Long id) throws Exception {
    return authorRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("author not found"));
  }
}
