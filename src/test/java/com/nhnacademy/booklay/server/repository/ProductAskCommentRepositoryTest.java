package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.ProductAskComment;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ProductAskCommentRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductAskCommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

  void clearRepo(String entityName, JpaRepository jpaRepository) {
    jpaRepository.deleteAll();

    String query =
            String.format("ALTER TABLE `%s` ALTER COLUMN `%s_no` RESTART WITH 1", entityName,
                    entityName);

    this.entityManager
            .getEntityManager()
            .createNativeQuery(query)
            .executeUpdate();
  }

  @BeforeEach
  void setUp() {
    clearRepo("post", postRepository);
    clearRepo("member", memberRepository);
  }

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

  @Test
  void testCommentFind(){
    ProductAskComment comment = DummyCart.getDummyComment();
    entityManager.persist(comment.getMemberId().getGender());
    memberRepository.save(comment.getMemberId());

        entityManager.persist(comment.getPostId().getPostTypeId());
        postRepository.save(comment.getPostId());

        commentRepository.save(comment);

        ProductAskComment expect =
            commentRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("no"));

        assertThat(expect.getContent()).isEqualTo(comment.getContent());

    }
}
