package com.nhnacademy.booklay.server.service.board;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.board.request.CommentRequest;
import com.nhnacademy.booklay.server.dto.board.response.CommentResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Comment;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.post.CommentRepository;
import com.nhnacademy.booklay.server.repository.post.PostRepository;
import java.sql.Ref;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

  @InjectMocks
  CommentServiceImpl commentService;

  @Mock
  CommentRepository commentRepository;
  @Mock
  PostRepository postRepository;
  @Mock
  MemberRepository memberRepository;


  Comment comment;
  Comment groupComment;
  CommentRequest request;
  Post post;
  Member member;

  @BeforeEach
  void setUp() {
    comment = DummyCart.getDummyComment();
    ReflectionTestUtils.setField(comment, "commentId", 1L);
    groupComment = DummyCart.getDummyComment();
    ReflectionTestUtils.setField(groupComment, "commentId", 2L);
    post = DummyCart.getDummyPost();
    ReflectionTestUtils.setField(post, "postId", 1L);
    member = Dummy.getDummyMember();

    request = new CommentRequest(comment.getCommentId(), post.getPostId(), member.getMemberNo(),
        "test comment content", 1L, 0, 0);

  }

  @Test
  @DisplayName("댓글 페이지로 조회")
  public void testRetrieveCommentPage() {
    // Given
    Long postId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    Comment comment = DummyCart.getDummyComment();

    Page<CommentResponse> page = new PageImpl<>(
        Collections.singletonList(new CommentResponse(comment)));

    // When
    given(commentRepository.findAllByPostIdAndPageable(any(Long.class),
        any(Pageable.class))).willReturn(page);
    Page<CommentResponse> commentResponsePage = commentService.retrieveCommentPage(postId,
        pageable);

    // Then
    assertThat(commentResponsePage.getContent().size()).isEqualTo(1);
    CommentResponse commentResponse = commentResponsePage.getContent().get(0);
    assertThat(commentResponse.getContent()).isEqualTo("dummy test comment");
  }

  @Test
  @DisplayName("1단계 댓글 생성 시험")
  void createComment() {

    request = new CommentRequest(comment.getCommentId(), post.getPostId(), member.getMemberNo(),
        "test comment content", null, null, 0);


    given(postRepository.findById(request.getPostId())).willReturn(Optional.ofNullable(post));
    given(memberRepository.findById(request.getMemberNo())).willReturn(Optional.ofNullable(member));

    Comment comment = Comment.builder()
        .postId(post)
        .memberId(member)
        .content(request.getContent())
        .depth(request.getDepth())
        .isDeleted(false)
        .build();

    if (request.getGroupCommentNo() != null) {
      given(commentRepository.findById(request.getGroupCommentNo())).willReturn(
          Optional.ofNullable(groupComment));
      comment.setGroupNo(groupComment);

      if (Objects.isNull(request.getGroupOrder())) {
        Integer currentOrderNo = 1;
        given(commentRepository.countChildByGroupNo(groupComment.getRealGroupNo())).willReturn(currentOrderNo);
        comment.setGroupOrder(currentOrderNo + 1);
      }

      if (Objects.nonNull(request.getGroupOrder())) {
        comment.setGroupOrder(request.getGroupOrder());
      }
    }

    if (request.getGroupCommentNo() == null) {
      Integer baseGroupOrder = 0;
      comment.setGroupOrder(baseGroupOrder);
      Comment saveForGroupNo = commentRepository.save(comment);
      comment.setGroupNo(saveForGroupNo);
    }

    given(commentRepository.save(any())).willReturn(comment);
    Long result = commentService.createComment(request);

    assertThat(result).isEqualTo(request.getCommentId());

  }

  @Test
  @DisplayName("답 댓글 순서에 맞게 생성 시험")
  void createCommentAtEnd() {

    request = new CommentRequest(comment.getCommentId(), post.getPostId(), member.getMemberNo(),
        "test comment content", 1L, null, 0);


    given(postRepository.findById(request.getPostId())).willReturn(Optional.ofNullable(post));
    given(memberRepository.findById(request.getMemberNo())).willReturn(Optional.ofNullable(member));

    Comment comment = Comment.builder()
        .postId(post)
        .memberId(member)
        .content(request.getContent())
        .depth(request.getDepth())
        .isDeleted(false)
        .build();

    if (request.getGroupCommentNo() != null) {
      given(commentRepository.findById(request.getGroupCommentNo())).willReturn(
          Optional.ofNullable(groupComment));
      comment.setGroupNo(groupComment);

      if (Objects.isNull(request.getGroupOrder())) {
        Integer currentOrderNo = 1;
        given(commentRepository.countChildByGroupNo(groupComment.getRealGroupNo())).willReturn(currentOrderNo);
        comment.setGroupOrder(currentOrderNo + 1);
      }

      if (Objects.nonNull(request.getGroupOrder())) {
        comment.setGroupOrder(request.getGroupOrder());
      }
    }

    if (request.getGroupCommentNo() == null) {
      Integer baseGroupOrder = 0;
      comment.setGroupOrder(baseGroupOrder);
      Comment saveForGroupNo = commentRepository.save(comment);
      comment.setGroupNo(saveForGroupNo);
    }

    given(commentRepository.save(any())).willReturn(comment);
    Long result = commentService.createComment(request);

    assertThat(result).isEqualTo(request.getCommentId());

  }

  @Test
  @DisplayName("댓글 중간에 답 댓글 삽입 시험")
  void createReplyCommentAtMiddle() {

    given(postRepository.findById(request.getPostId())).willReturn(Optional.ofNullable(post));
    given(memberRepository.findById(request.getMemberNo())).willReturn(Optional.ofNullable(member));

    Comment comment = Comment.builder()
        .postId(post)
        .memberId(member)
        .content(request.getContent())
        .depth(request.getDepth())
        .isDeleted(false)
        .build();

    if (request.getGroupCommentNo() != null) {
      given(commentRepository.findById(request.getGroupCommentNo())).willReturn(
          Optional.ofNullable(groupComment));
      comment.setGroupNo(groupComment);

      if (Objects.isNull(request.getGroupOrder())) {
        Integer currentOrderNo = 1;
        given(commentRepository.countChildByGroupNo(groupComment.getRealGroupNo())).willReturn(currentOrderNo);
        comment.setGroupOrder(currentOrderNo + 1);
      }

      if (Objects.nonNull(request.getGroupOrder())) {
        comment.setGroupOrder(request.getGroupOrder());
      }
    }

    if (request.getGroupCommentNo() == null) {
      Integer baseGroupOrder = 0;
      comment.setGroupOrder(baseGroupOrder);
      Comment saveForGroupNo = commentRepository.save(comment);
      comment.setGroupNo(saveForGroupNo);
    }

    given(commentRepository.save(any())).willReturn(comment);
    Long result = commentService.createComment(request);

    assertThat(result).isEqualTo(request.getCommentId());

  }

  @Test
  @DisplayName("댓글 수정")
  void updateComment() {
    //given
    given(commentRepository.findById(request.getCommentId())).willReturn(
        Optional.ofNullable(comment));
    comment.setContent(request.getContent());
    given(commentRepository.save(comment)).willReturn(comment);

    //when
    Long result = commentService.updateComment(request);

    //then
    assertThat(result).isEqualTo(comment.getCommentId());
  }

  @Test
  @DisplayName("댓글 소프트 딜리트")
  void deleteComment() {
    Long commentId = 1L;
    Long memberNo = 1L;
    ReflectionTestUtils.setField(comment, "commentId", 1L);

    given(commentRepository.findById(commentId)).willReturn(Optional.ofNullable(comment));

    commentService.deleteComment(commentId, memberNo);

    if (comment.getMemberId().getMemberNo().equals(memberNo)) {
      BDDMockito.then(commentRepository).should().softDelete(commentId);
    }
  }
}