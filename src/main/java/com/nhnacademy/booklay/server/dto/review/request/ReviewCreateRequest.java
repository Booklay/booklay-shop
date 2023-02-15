package com.nhnacademy.booklay.server.dto.review.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateRequest {

    private Long productId;

    private Long score;
    private String content;

    private Long memberNo;

}
