package com.nhnacademy.booklay.server.repository.post;

import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.QPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {

  public PostRepositoryImpl() {
    super(Post.class);
  }

  @Override
  public Page<PostResponse> findAllByProductIdPage(Long productId, Pageable pageable) {
    QPost post = QPost.post;

    List<PostResponse> content = from(post).where(post.productId.id.eq(productId))
        .orderBy(post.groupNo.postId.desc(),
            post.groupOrder.asc())
        .select(Projections.constructor(PostResponse.class, post))
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .fetch();

    JPQLQuery<Long> count = from(post)
        .where(post.productId.id.eq(productId))
        .select(post.count());
    return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
  }
}
