package com.nhnacademy.booklay.server.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.server.dto.board.request.BoardPostUpdateRequest;
import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.PostType;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.post.PostRepository;
import com.nhnacademy.booklay.server.repository.post.PostTypeRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

  @InjectMocks
  PostServiceImpl postService;

  @Mock
  PostRepository postRepository;
  @Mock
  ProductRepository productRepository;
  @Mock
  PostTypeRepository postTypeRepository;
  @Mock
  MemberRepository memberRepository;

  BoardPostCreateRequest request;
  Post post;
  PostType postType;
  Member member;
  Product product;

  @BeforeEach
  void setUp() {
    post = DummyCart.getDummyPost();
    ReflectionTestUtils.setField(post, "postId", 1L);

    postType = PostType.builder()
        .postTypeId(1)
        .type("qna")
        .build();

    member = Dummy.getDummyMember();

    product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    ReflectionTestUtils.setField(product, "id", 1L);

    request = new BoardPostCreateRequest(postType.getPostTypeId(), member.getMemberNo(),
        product.getId(), post.getPostId(), 1, 1, "when you tie the old man", "it is title", true,
        false);
  }

  @Test
  void createPost() {
    given(postTypeRepository.findById(request.getPostTypeNo())).willReturn(
        Optional.ofNullable(postType));
    given(memberRepository.findById(request.getMemberNo())).willReturn(Optional.ofNullable(member));

    Post savePost = Post.builder()
        .postTypeId(postType)
        .memberId(member)
        .groupOrder(request.getGroupOrderNo())
        .depth(request.getDepth())
        .title(request.getTitle())
        .content(request.getContent())
        .isViewPublic(request.getViewPublic())
        .build();

    if (request.getProductNo() != null) {
      given(productRepository.findById(request.getProductNo())).willReturn(
          Optional.ofNullable(product));
      savePost.setProductId(product);
    }
    if (request.getGroupOrderNo() != null) {
      given(postRepository.findById(Long.valueOf(request.getGroupOrderNo()))).willReturn(
          Optional.ofNullable(post));
      savePost.setGroupNo(post);
    }
    if (request.getAnswered() != null) {
      savePost.setAnswered(request.getAnswered());
    }

    ReflectionTestUtils.setField(savePost, "postId", 2L);
    given(postRepository.save(any())).willReturn(savePost);

    Long result = postService.createPost(request);

    assertThat(result).isEqualTo(savePost.getPostId());
  }

  @Disabled
  @Test
  void updatePost(BoardPostUpdateRequest request) {
    given(postRepository.findById(request.getPostId())).willReturn(Optional.ofNullable(post));

    post.setTitle(request.getTitle());
    post.setContent(request.getContent());
    post.setViewPublic(request.getViewPublic());

    postRepository.save(post);

  }

  @Disabled
  @Test
  void updateConfirmAnswer(Long postId) {
    postRepository.confirmAnswerByPostId(postId);
  }

  @Test
  void retrieveProductQNA() {
    Long productId = 1L;
    Pageable pageable = Pageable.ofSize(10);
    postService.retrieveProductQNA(productId, pageable);

    BDDMockito.then(postRepository).should().findAllByProductIdPage(productId, pageable);
  }

  @Test
  void retrievePostById() {
    Long postId = 1L;

    given(postRepository.findById(postId)).willReturn(Optional.ofNullable(post));

    List<RetrieveAuthorResponse> authors = new ArrayList<>();
    RetrieveAuthorResponse author = new RetrieveAuthorResponse(1L, "test");
    authors.add(author);

    PostResponse response = new PostResponse(post);
    if (Objects.nonNull(post.getProductId())) {
      given(productRepository.getAuthorsByProductId(post.getProductId().getId())).willReturn(
          authors);
      response.setAuthorList(authors);
    }

    PostResponse result = postService.retrievePostById(postId);

    assertThat(result.getPostId()).isEqualTo(response.getPostId());
  }

  @Test
  void deletePost() {
    Long memberId = 1L;
    Long postId = 1L;

    postService.deletePost(memberId, postId);

    BDDMockito.then(postRepository).should().deleteByPostIdAndMemberNo(postId, memberId);
  }

}