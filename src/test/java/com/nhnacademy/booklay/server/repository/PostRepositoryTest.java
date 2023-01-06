package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
  @Autowired
  MemberRepository memberRepository;

  @Test
  void testPostSave() {
    Post post = DummyCart.getDummyPost();
    entityManager.persist(post.getMemberId().getGender());
    memberRepository.save(post.getMemberId());

    entityManager.persist(post.getPostTypeId());

    Post expect = postRepository.save(post);

    assertThat(expect.getContent()).isEqualTo(post.getContent());
  }

  @Test
  void testPostFind() {
    Post post = DummyCart.getDummyPost();
    entityManager.persist(post.getMemberId().getGender());
    memberRepository.save(post.getMemberId());

    entityManager.persist(post.getPostTypeId());

    postRepository.save(post);

    Post expect = postRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("no"));

    assertThat(expect.getContent()).isEqualTo(post.getContent());
  }
}