package com.nhnacademy.booklay.server.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;
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
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
        .groupOrder(request.getGroupOrder())
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
    if (request.getGroupPostNo() != null) {
      given(postRepository.findById(request.getGroupPostNo())).willReturn(
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

  @Test
  void retrieveProductQNA() {
  }
}