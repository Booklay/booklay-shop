package com.nhnacademy.booklay.server.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
import com.nhnacademy.booklay.server.service.RedisCacheService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
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
  RedisCacheService redisCacheService;
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
  Post groupPost;
  PostType postType;
  Member member;
  Product product;
  Pageable pageable;



  @BeforeEach
  void setUp() {
    post = DummyCart.getDummyPost();
    ReflectionTestUtils.setField(post, "postId", 1L);

    groupPost = DummyCart.getDummyPost();
    ReflectionTestUtils.setField(post, "postId", 2L);

    postType = PostType.builder()
        .postTypeId(1)
        .type("qna")
        .build();

    member = Dummy.getDummyMember();

    product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    ReflectionTestUtils.setField(product, "id", 1L);

    pageable = Pageable.ofSize(10);
  }

  @DisplayName("1단계 게시글 생성")
  @Test
  void createPost() {
    request = new BoardPostCreateRequest(postType.getPostTypeId(), member.getMemberNo(),
        product.getId(), null, null, 1, "when you tie the old man", "it is title", true,
        false);

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
    if(request.getGroupNo() != null){
      given(postRepository.findById(request.getGroupNo())).willReturn(Optional.ofNullable(groupPost));

      if (Objects.isNull(request.getGroupOrderNo())) {
        Integer currentOrderNo = postRepository.countChildByGroupNo(groupPost.getRealGroupNo());
        post.setGroupOrder(currentOrderNo + 1);
      }
      if (request.getGroupOrderNo() != null) {
        given(postRepository.findById(Long.valueOf(request.getGroupOrderNo()))).willReturn(
            Optional.ofNullable(post));
        savePost.setGroupNo(post);
      }
    }
    if (request.getAnswered() != null) {
      savePost.setAnswered(request.getAnswered());
    }

    ReflectionTestUtils.setField(savePost, "postId", 2L);
    given(postRepository.save(any())).willReturn(savePost);

    Long result = postService.createPost(request);

    assertThat(result).isEqualTo(savePost.getPostId());
  }

  @DisplayName("순서에 맞게 답글 작성")
  @Test
  void createReplyPost() {
    request = new BoardPostCreateRequest(postType.getPostTypeId(), member.getMemberNo(),
        product.getId(), post.getPostId(), null, 1, "when you tie the old man", "it is title", true,
        false);

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
    if(request.getGroupNo() != null){
      given(postRepository.findById(request.getGroupNo())).willReturn(Optional.ofNullable(groupPost));

      if (Objects.isNull(request.getGroupOrderNo())) {
        Integer currentOrderNo = postRepository.countChildByGroupNo(groupPost.getRealGroupNo());
        post.setGroupOrder(currentOrderNo + 1);
      }
    }
    if (request.getAnswered() != null) {
      savePost.setAnswered(request.getAnswered());
    }

    ReflectionTestUtils.setField(savePost, "postId", 2L);
    given(postRepository.save(any())).willReturn(savePost);

    Long result = postService.createPost(request);

    assertThat(result).isEqualTo(savePost.getPostId());
  }


  @DisplayName("게시글 중간에 답글 삽입")
  @Test
  void createReplyPostAtMiddle() {
    request = new BoardPostCreateRequest(postType.getPostTypeId(), member.getMemberNo(),
        product.getId(), post.getPostId(), 1, 1, "when you tie the old man", "it is title", true,
        false);

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
    if(request.getGroupNo() != null){
      given(postRepository.findById(request.getGroupNo())).willReturn(Optional.ofNullable(groupPost));

      if (Objects.isNull(request.getGroupOrderNo())) {
        Integer currentOrderNo = postRepository.countChildByGroupNo(groupPost.getRealGroupNo());
        post.setGroupOrder(currentOrderNo + 1);
      }
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
  void updatePost() {
    //given
    BoardPostUpdateRequest updateRequest = new BoardPostUpdateRequest(1L, "new title",
        "new content", false);

    given(postRepository.findById(updateRequest.getPostId())).willReturn(Optional.ofNullable(post));

    post.setTitle(updateRequest.getTitle());
    post.setContent(updateRequest.getContent());
    post.setViewPublic(updateRequest.getViewPublic());

    //when
    Long result  = postService.updatePost(updateRequest);

    //then
    then(postRepository).should().save(post);
  }

  @Test
  void updateConfirmAnswer() {
    //given
    Long postId = 1L;

    //when
    postService.updateConfirmAnswer(postId);

    //then
    BDDMockito.then(postRepository).should().confirmAnswerByPostId(postId);
  }

  @Test
  void retrieveProductQNA() {
    Long productId = 1L;
    postService.retrieveProductQNA(productId, pageable);

    BDDMockito.then(postRepository).should().findAllByProductIdPage(productId, pageable);
  }

  @Test
  void retrieveNotice(){


    //when
    postService.retrieveNotice(pageable);

    //then
    BDDMockito.then(postRepository).should().findAllNotice(5 ,pageable);
  }

  @Test
  void retrieveNoticeList() {
    //given
    Integer pageLimit = 5;

    //when
    postService.retrieveNoticeList(pageLimit);

    //then
    BDDMockito.then(postRepository).should().findNoticeList(pageLimit);
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