package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.ProductAskComment;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class ProductAskCommentRepositoryTest {

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  ProductAskCommentRepository commentRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  PostRepository postRepository;

  @Test
  void testCommentSave(){
    ProductAskComment comment = DummyCart.getDummyComment();
    entityManager.persist(comment.getMemberId().getGender());
    memberRepository.save(comment.getMemberId());

    entityManager.persist(comment.getPostId().getPostTypeId());
    postRepository.save(comment.getPostId());

    ProductAskComment expect = commentRepository.save(comment);

    assertThat(expect.getContent()).isEqualTo(comment.getContent());
  }
}