package com.nhnacademy.booklay.server.dto.board.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

  private Long commentId;
  @NotNull
  private Long postId;
  @NotNull
  private Long memberNo;
  @NotNull
  private String content;
  private Long groupCommentNo;
  private Integer groupOrder;
  @NotNull
  private Integer depth;
}
