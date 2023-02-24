package com.nhnacademy.booklay.server.service.review;

import com.nhnacademy.booklay.server.dto.review.request.ReviewCreateRequest;
import com.nhnacademy.booklay.server.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {

  void createReview(ReviewCreateRequest request, MultipartFile file);
  SearchPageResponse<RetrieveReviewResponse> retrieveReviewListByProductId(Long productId,
      Pageable pageable);
  RetrieveReviewResponse retrieveReviewListByReviewId(Long reviewId);
  boolean deleteReviewById(Long reviewId);
}
