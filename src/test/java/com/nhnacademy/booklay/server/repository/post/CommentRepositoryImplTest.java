package com.nhnacademy.booklay.server.repository.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Comment;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.PostType;
import com.nhnacademy.booklay.server.repository.member.GenderRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author : 최규태
 */
@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryImplTest {

  @Autowired
  CommentRepository commentRepository;

  @Autowired
  TestEntityManager entityManager;

  EntityManager em;
  Comment comment;
  Gender gender;
  Member member;
  PostType postType;
  Post post;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PostTypeRepository postTypeRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private GenderRepository genderRepository;

//  @BeforeEach
  void setup(){
    em = entityManager.getEntityManager();
    postType = DummyCart.getDummyPostType();
    post = DummyCart.getDummyPost();

    member = Dummy.getDummyMember();
    gender = Dummy.getDummyGender();

    genderRepository.save(gender);
    memberRepository.save(member);
    postTypeRepository.save(postType);
    postRepository.save(post);

    comment = DummyCart.getDummyComment();
    commentRepository.save(comment);
  }

  @Test
  void updateUpperCommentByGroupNoCommentId() {
//    commentRepository.updateUpperCommentByGroupNoCommentId();
  }

  @Test
  void countChildByGroupNo() {
//    commentRepository.countChildByGroupNo();
  }

  @Test
  void findAllByPostIdAndPageable() {
    Pageable pageable = PageRequest.of(1,20);
//    commentRepository.findAllByPostIdAndPageable( ,pageable);
  }

  @Disabled
  @Test
  void softDelete() {
    em.clear();

    //when
    commentRepository.softDelete(comment.getCommentId());

    //then
    assertThat(comment.isDeleted()).isTrue();
  }
}