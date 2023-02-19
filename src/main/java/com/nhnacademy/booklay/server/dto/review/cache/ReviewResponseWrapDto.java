package com.nhnacademy.booklay.server.dto.review.cache;

import com.nhnacademy.booklay.server.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import lombok.Data;

@Data
public class ReviewResponseWrapDto {
    private SearchPageResponse<RetrieveReviewResponse> data;
    private ReviewResponseWrapDto previous;
    private ReviewResponseWrapDto next;
    private Long productId;
}
