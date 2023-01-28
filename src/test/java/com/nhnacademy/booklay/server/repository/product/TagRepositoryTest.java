package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.repository.product.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class TagRepositoryTest {

  @Autowired
  TagRepository repository;

  @Test
  void get(){
    Page<RetrieveTagResponse> page = repository.findAllBy(PageRequest.of(1,20), RetrieveTagResponse.class);
  }
}
