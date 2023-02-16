package com.nhnacademy.booklay.server.dto.board.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CommentResponse {

  Long commentId;
  Long memberNo;
  String name;
  String content;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
  Long groupCommentNo;
  Integer groupOrder;
  Integer depth;
  Boolean deleted;
}
