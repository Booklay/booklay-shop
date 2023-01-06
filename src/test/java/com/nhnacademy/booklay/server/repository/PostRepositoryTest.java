package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Post;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class PostRepositoryTest {
  @Autowired
  TestEntityManager entityManager;

  @Autowired
  PostRepository postRepository;

  @Test
  void testPostSave(){
    Post post = DummyCart.getDummyPost();
    entityManager.persist(post.getPostTypeId());

    Post expect = postRepository.save(post);

    assertThat(expect.getPostTypeId()).isEqualTo(post.getPostTypeId());
  }
}