package com.nhnacademy.booklay.server.controller.review;

import com.nhnacademy.booklay.server.dto.review.request.ReviewCreateRequest;
import com.nhnacademy.booklay.server.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.exception.controller.CreateFailedException;
import com.nhnacademy.booklay.server.service.review.ReviewService;
import com.nhnacademy.booklay.server.service.review.cache.ReviewResponsePageCacheWrapService;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewResponsePageCacheWrapService reviewResponsePageCacheWrapService;
    private final ReviewService reviewService;

    public ReviewController(ReviewResponsePageCacheWrapService reviewResponsePageCacheWrapService,
                            ReviewService reviewService) {
        this.reviewResponsePageCacheWrapService = reviewResponsePageCacheWrapService;
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Void> createReview(@RequestPart ReviewCreateRequest request,
                                             @RequestPart(required = false) MultipartFile file) {
        try {
            reviewService.createReview(request, file);
            return ResponseEntity.status(HttpStatus.CREATED)
                .build();
        } catch (CreateFailedException e) {
            return ResponseEntity.status(HttpStatus.OK)
                .build();
        }

    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<SearchPageResponse<RetrieveReviewResponse>> retrieveReviewByProductId(@PathVariable Long productId, Pageable pageable) {

        SearchPageResponse<RetrieveReviewResponse> response =
                reviewResponsePageCacheWrapService.cacheRetrievePostResponsePage(productId, pageable);

        return Objects.isNull(response)
            ? ResponseEntity.status(HttpStatus.OK).build()
            : ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<RetrieveReviewResponse> retrieveReviewByReviewId(@PathVariable Long reviewId) {

        RetrieveReviewResponse response = reviewService.retrieveReviewListByReviewId(reviewId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReviewByReviewId(@PathVariable Long reviewId) {
        return reviewService.deleteReviewById(reviewId)
            ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
}
