package com.nhnacademy.booklay.server.dto.board.response;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.entity.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostResponse {

  Long postId;
  Integer postTypeNo;
  Long memberNo;
  Long productNo;
  Long groupPostNo;
  Integer groupOrder;
  Integer depth;
  String title;
  String content;
  Boolean viewPublic;
  Boolean answered;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;

  String writer;

  @Setter
  List<RetrieveAuthorResponse> authorList = new ArrayList<>();

  public PostResponse(Post post) {
    this.postId = post.getPostId();
    this.postTypeNo = post.getPostTypeId().getPostTypeId();
    this.memberNo = post.getMemberId().getMemberNo();
    this.productNo = Objects.nonNull(post.getProductId()) ? post.getProductId().getId() : null;
    this.groupPostNo = Objects.nonNull(post.getGroupNo()) ? post.getGroupNo().getPostId() : null;
    this.groupOrder = Objects.nonNull(post.getGroupOrder()) ? post.getGroupOrder() : null;
    this.depth = post.getDepth();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.viewPublic = post.isViewPublic();
    this.answered = Objects.nonNull(post.isAnswered()) ? post.isAnswered() : null;
    this.createdAt = post.getCreatedAt();
    this.updatedAt = post.getUpdatedAt();
    this.writer = post.getMemberId().getNickname();
  }
}