package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "post")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @Column(name = "post_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_type_no")
    private PostType postTypeId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    private Product productId;
    @Column(name = "product_no", updatable = false, insertable = false)
    private Long productNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member memberId;
    @Column(name = "member_no", updatable = false, insertable = false)
    private Long memberNo;
    @Setter
    @ManyToOne
    @JoinColumn(name = "group_post_no")
    private Post groupNo;
    @Column(name = "group_post_no", insertable = false, updatable = false)
    private Long realGroupNo;

    @Setter
    @Column(name = "group_order")
    private Integer groupOrder;

    @Column
    private Integer depth;

    @Column
    @Setter
    private String title;

    @Column
    @Setter
    private String content;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_view_public")
    @Setter
    private boolean isViewPublic;

    @Setter
    @Column(name = "is_answered")
    private boolean isAnswered;

    @Column(name="is_deleted")
    private boolean isDeleted;


    @Builder
    public Post(PostType postTypeId, Member memberId, Integer groupOrder, Integer depth, String title,
                String content, boolean isViewPublic, Post groupNo, boolean  isDeleted) {
        this.postTypeId = postTypeId;
        this.memberId = memberId;
        this.groupOrder = groupOrder;
        this.depth = depth;
        this.title = title;
        this.content = content;
        this.isViewPublic = isViewPublic;
        this.groupNo = groupNo;
        this.isDeleted = isDeleted;
    }
}
