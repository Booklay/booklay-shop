package com.nhnacademy.booklay.server.dto.review.response;

import com.nhnacademy.booklay.server.entity.Review;
import java.util.Objects;
import lombok.Getter;

@Getter
public class RetrieveReviewResponse {
    Long id;
    String content;
    Long score;
    Long productId;
    String writerName;
    Long writerNo;
    Long imageNo;

    public RetrieveReviewResponse(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.score = review.getScore();

        this.productId = review.getProductNo();
        this.writerName = Objects.nonNull(review.getMember()) ? review.getMember().getNickname() : "알 수 없는 작성자";
        this.writerNo = review.getMemberNo();
        this.imageNo = review.getImageNo();
    }
}
