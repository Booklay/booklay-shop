package com.nhnacademy.booklay.server.repository.post;

import com.nhnacademy.booklay.server.entity.Comment;
import com.nhnacademy.booklay.server.entity.QComment;
import com.nhnacademy.booklay.server.entity.QPost;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CommentRepositoryImpl extends QuerydslRepositorySupport implements
    CommentRepositoryCustom {

  public CommentRepositoryImpl() {
    super(Comment.class);
  }

  @Override
  public void updateUpperCommentByGroupNoCommentId(Long groupNo, Integer rebaseOrder) {
    QComment comment = QComment.comment;
    update(comment).where(comment.realGroupNo.eq(groupNo), comment.groupOrder.goe(rebaseOrder))
        .set(comment.groupOrder, comment.groupOrder.add(1)).execute();
  }

  @Override
  public Integer countChildByGroupNo(Long groupNo) {

    QComment comment = QComment.comment;

    return Math.toIntExact(
        from(comment).where(comment.realGroupNo.eq(groupNo)).select(comment.count()).fetchFirst());
  }
}
