package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "product_ask_comment")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProductAskComment {

  @Id
  @Column(name = "comment_no")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_no")
  private Post postId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_no")
  private Member memberId;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_comment_no")
  private ProductAskComment groupNo;

  @Column
  private String content;

  @Column
  private Long depth;

  @CreatedDate
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "group_order")
  private Long groupOrder;

  @Builder
  public ProductAskComment(Post postId, Member memberId, String content, Long depth,
      Long groupOrder) {
    this.postId = postId;
    this.memberId = memberId;
    this.content = content;
    this.depth = depth;
    this.groupOrder = groupOrder;
  }
}