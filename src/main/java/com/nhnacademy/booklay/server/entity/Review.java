package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * javadoc. 리뷰 entity
 */
@Table(name = "review")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @Column(name = "review_no")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no", nullable = false)
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_no", nullable = false)
    private Image image;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String content;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // TODO 컬럼 이름 변경 필요 -> modified_at
    @Setter
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    public Review(Long id, Product product, Member member, Image image, int score, String content,
                  LocalDateTime createdAt) {
        this.id = id;
        this.product = product;
        this.member = member;
        this.image = image;
        this.score = score;
        this.content = content;
        this.createdAt = createdAt;
    }
}
