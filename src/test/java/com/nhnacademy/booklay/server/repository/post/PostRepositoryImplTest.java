package com.nhnacademy.booklay.server.repository.post;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.PostType;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class PostRepositoryImplTest {

  @Autowired
  PostRepository postRepository;
  @Autowired
  TestEntityManager entityManager;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private ProductRepository productRepository;

  Product product;
  Post productPost;
  Post noticePost;
  Member member;

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
  void setUp() {
    clearRepo("post", postRepository);

    entityManager.persist(Dummy.getDummyGender());
    member = memberRepository.save(Dummy.getDummyMember());

    ObjectFile objectFile = entityManager.persist(DummyCart.getDummyFile());
    Product seedProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    seedProduct.setThumbnailNo(objectFile.getId());
    product = productRepository.save(seedProduct);

  }

  @Test
  void findAllByProductIdPage() {
    //given
    PostType postType = entityManager.persist(DummyCart.getDummyPostType());
    Post qnaSeedPost = Post.builder()
        .memberId(member)
        .postTypeId(postType)
        .groupOrder(0)
        .depth(0)
        .title("dummy title")
        .content("dummy content for test")
        .isViewPublic(true)
        .isDeleted(false)
        .build();

    qnaSeedPost.setProductId(product);

    productPost = postRepository.save(qnaSeedPost);
    Pageable pageable = PageRequest.of(0, 20);

    //when
    Page<PostResponse> postResponses = postRepository.findAllByProductIdPage(product.getId(),
        pageable);

    //then
    assertThat(postResponses.getContent()).hasSize(1);
    assertThat(postResponses.getContent().get(0).getPostId()).isEqualTo(productPost.getPostId());
  }

  @Test
  void findAllNotice() {
    PostType noticeType = entityManager.persist(
        PostType.builder().type("공지사항").postTypeId(5).build());

    Post noticeSeedPost = Post.builder()
        .memberId(member)
        .postTypeId(noticeType)
        .groupOrder(0)
        .depth(0)
        .title("dummy title")
        .content("dummy content for test")
        .isViewPublic(true)
        .isDeleted(false)
        .build();
    noticePost = postRepository.save(noticeSeedPost);

    Pageable pageable = PageRequest.of(0, 10);

    //when
    Page<PostResponse> responses = postRepository.findAllNotice(noticeType.getPostTypeId(),
        pageable);

    //then
    assertThat(responses.getContent()).hasSize(1);
    assertThat(responses.getContent().get(0).getPostId()).isEqualTo(noticePost.getPostId());
  }

//  @Test
//  void updateUpperPostByGroupNoPostId() {
//  }

  @Test
  void confirmAnswerByPostId() {
    //given
    PostType postType = entityManager.persist(DummyCart.getDummyPostType());
    Post qnaSeedPost = Post.builder()
        .memberId(member)
        .postTypeId(postType)
        .groupOrder(0)
        .depth(0)
        .title("dummy title")
        .content("dummy content for test")
        .isViewPublic(true)
        .isDeleted(false)
        .build();

    qnaSeedPost.setProductId(product);

    productPost = postRepository.save(qnaSeedPost);

    //when
    Long result = postRepository.confirmAnswerByPostId(productPost.getPostId());

    //then
    assertThat(result).isEqualTo(productPost.getPostId());
  }

  //  @Test
//  void countChildByGroupNo() {
//  }
//
//  @Test
//  void deleteByPostIdAndMemberNo() {
//  }
//
  @Test
  void findNoticeList() {
    Integer limit = 5;

    PostType noticeType = entityManager.persist(
        PostType.builder().type("공지사항").postTypeId(5).build());

    Post noticeSeedPost = Post.builder()
        .memberId(member)
        .postTypeId(noticeType)
        .groupOrder(0)
        .depth(0)
        .title("dummy title")
        .content("dummy content for test")
        .isViewPublic(true)
        .isDeleted(false)
        .build();

    postRepository.save(noticeSeedPost);

    //when
    List<PostResponse> responses = postRepository.findNoticeList(limit);

    //then
    assertThat(responses.get(0).getPostTypeNo()).isEqualTo(noticeType.getPostTypeId());
  }
}