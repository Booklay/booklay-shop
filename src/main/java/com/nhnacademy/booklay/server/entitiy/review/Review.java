package com.nhnacademy.booklay.server.entitiy.review;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * javadoc. 리뷰 entity
 */
@Table(name = "review")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @Column(name = "review_no")
    private Long id;

//    private Product product;
//    private Member member;
//    private Image image;

    private int score;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifyAt;

}
