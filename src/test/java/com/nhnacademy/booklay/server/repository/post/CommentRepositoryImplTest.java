package com.nhnacademy.booklay.server.repository.post;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Comment;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.PostType;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author : 최규태
 */
@DataJpaTest
@ActiveProfiles("test")
@Transactional
class CommentRepositoryImplTest {

  @Autowired
  CommentRepository commentRepository;
  @Autowired
  TestEntityManager entityManager;


  Comment comment;

  Member member;
  PostType postType;
  Post post;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private MemberRepository memberRepository;

  void clearRepo(String entityName, JpaRepository jpaRepository) {
    jpaRepository.deleteAll();

    String query =
        String.format("ALTER TABLE `%s` ALTER COLUMN `%s_no` RESTART WITH 1",
            entityName, entityName);

    this.entityManager
        .getEntityManager()
        .createNativeQuery(query)
        .executeUpdate();
  }

  @BeforeEach
  void setup() {
//    clearRepo("post", postRepository);
    clearRepo("comment", commentRepository);


    entityManager.persist(Dummy.getDummyGender());
    member = memberRepository.save(Dummy.getDummyMember());

    postType = entityManager.persist(DummyCart.getDummyPostType());

    Post seedPost = Post.builder()
        .memberId(member)
        .postTypeId(postType)
        .groupOrder(0)
        .depth(0)
        .title("dummy title")
        .content("dummy content for test")
        .isViewPublic(true)
        .isDeleted(false)
        .build();

    post = postRepository.save(seedPost);

    Comment seedComment = Comment.builder()
        .postId(post)
        .memberId(member)
        .groupNo(null)
        .content("dummy test comment")
        .groupOrder(0)
        .depth(1)
        .isDeleted(false)
        .build();

    comment = commentRepository.save(seedComment);
  }

  @Disabled
  @Test
  void softDelete() {

    //when
    Long result = commentRepository.softDelete(comment.getCommentId());

    //then
    assertThat(result).isEqualTo(comment.getCommentId());
  }
}