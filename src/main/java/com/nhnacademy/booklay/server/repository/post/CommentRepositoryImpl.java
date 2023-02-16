package com.nhnacademy.booklay.server.repository.post;

import com.nhnacademy.booklay.server.dto.board.response.CommentResponse;
import com.nhnacademy.booklay.server.entity.Comment;
import com.nhnacademy.booklay.server.entity.QComment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

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

  @Override
  public Page<CommentResponse> findAllByPostIdAndPageable(Long postId, Pageable pageable) {
    QComment comment = QComment.comment;

    List<CommentResponse> commentList = from(comment).where(comment.postId.postId.eq(postId))
        .select(Projections.constructor(CommentResponse.class,
            comment))
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .fetch();

    JPQLQuery<Long> count = from(comment)
        .where(comment.postId.postId.eq(postId))
        .select(comment.count());
    return PageableExecutionUtils.getPage(commentList, pageable, count::fetchFirst);
  }

  @Override
  public Long softDelete(Long commentId) {
    QComment comment = QComment.comment;

    return update(comment).where(comment.commentId.eq(commentId)).set(comment.isDeleted, true).execute();
  }
}
