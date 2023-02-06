package com.nhnacademy.booklay.server.repository.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Author;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class AuthorRepositoryTest {
  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  TestEntityManager entityManager;

//  @Test
//   void testFindAllBy_Success(){
//     Page result = authorRepository.findAllBy(any(), any());
//
//     assertThat(result.getTotalElements()).isZero();
//     authorRepository.save(author);
//
//     Page<RetrieveAuthorResponse> page = authorRepository.findAllBy(PageRequest.of(0,20));
//   }
}