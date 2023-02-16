package com.nhnacademy.booklay.server.dto.board.response;

import com.nhnacademy.booklay.server.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  public CommentResponse(Comment comment) {
    this.commentId = comment.getCommentId();
    this.memberNo = comment.getMemberId().getMemberNo();
    this.name = comment.getMemberId().getNickname();
    this.content = comment.getContent();
    this.createdAt = comment.getCreatedAt();
    this.updatedAt = comment.getUpdatedAt();
    this.groupCommentNo = comment.getRealGroupNo();
    this.groupOrder = comment.getGroupOrder();
    this.depth = comment.getDepth();
    this.deleted = comment.isDeleted();
  }
}
