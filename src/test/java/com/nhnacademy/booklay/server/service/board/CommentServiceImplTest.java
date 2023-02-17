package com.nhnacademy.booklay.server.service.board;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.board.response.CommentResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Comment;
import com.nhnacademy.booklay.server.repository.post.CommentRepository;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
  @InjectMocks
  CommentServiceImpl commentService;

  @Mock
  CommentRepository commentRepository;

  @Test
  public void testRetrieveCommentPage() {
    // Given
    Long postId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    Comment comment = DummyCart.getDummyComment();

    Page<CommentResponse> page = new PageImpl<>(Collections.singletonList(new CommentResponse(comment)));

    // When
    given(commentRepository.findAllByPostIdAndPageable(any(Long.class), any(Pageable.class))).willReturn(page);
    Page<CommentResponse> commentResponsePage = commentService.retrieveCommentPage(postId, pageable);

    // Then
    assertThat(commentResponsePage.getContent().size()).isEqualTo(1);
    CommentResponse commentResponse = commentResponsePage.getContent().get(0);
    assertThat(commentResponse.getContent()).isEqualTo("dummy test comment");
  }

  @Test
  void createComment() {
  }

  @Test
  void updateComment() {
  }

  @Test
  void deleteComment() {
  }
}