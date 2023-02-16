package com.nhnacademy.booklay.server.service.review;

import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.review.request.ReviewCreateRequest;
import com.nhnacademy.booklay.server.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.server.entity.Review;
import com.nhnacademy.booklay.server.exception.controller.CreateFailedException;
import com.nhnacademy.booklay.server.repository.ReviewRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import com.nhnacademy.booklay.server.service.storage.FileService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final PointHistoryService pointHistoryService;
    private final FileService fileService;

    private static final String REVIEW_REGISTER = "리뷰 등록 포인트 적립";

    private static final Integer NON_IMAGE = 100;
    private static final Integer INCLUDE_IMAGE = 500;

    public void createReview(ReviewCreateRequest request, MultipartFile file) {

        try {
            if (isAbleToRegistered(request)) {


                Integer reviewPoint = NON_IMAGE;

                Long objectFileId = null;
                if (!Objects.nonNull(file)) {
                    objectFileId = fileService.uploadFile(file).getId();
                    reviewPoint = INCLUDE_IMAGE;
                }

                Review review = Review.builder()
                    .productNo(request.getProductId())
                    .memberNo(request.getMemberNo())
                    .imageNo(objectFileId)
                    .score(request.getScore())
                    .content(request.getContent())
                    .isDeleted(false)
                    .build();

                reviewRepository.save(review);

                PointHistoryCreateRequest pointRequest = new PointHistoryCreateRequest(
                    request.getMemberNo(), reviewPoint, REVIEW_REGISTER
                );

                pointHistoryService.createPointHistory(pointRequest);
            } else {
                throw new CreateFailedException("사용자 혹은 상품이 리뷰를 작성할 수 없는 상태");
            }

        } catch (Exception e) {
            throw new CreateFailedException(e.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public List<RetrieveReviewResponse> retrieveReviewListByProductId(Long productId,
                                                                      Pageable pageable) {

        try {
            List<Review> reviewList = reviewRepository.findReviewsByProductNoAndIsDeleted(productId, false, pageable);

            List<RetrieveReviewResponse> reviews = new ArrayList<>();

            reviewList.forEach(
                review -> reviews.add(new RetrieveReviewResponse(review))
            );

            return reviews;
        } catch (Exception e) {

            log.error(" Retrieve Review List By ProductId : {}", e.getMessage());
        }

        return List.of();


    }

    public RetrieveReviewResponse retrieveReviewListByReviewId(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow();

        return new RetrieveReviewResponse(review);
    }

    public boolean deleteReviewById(Long reviewId) {

        try {
            reviewRepository.softDeleteReview(reviewId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAbleToRegistered(ReviewCreateRequest request){

        return (productRepository.existsById(request.getProductId()) && memberRepository.existsById(request.getMemberNo()))
            && !reviewRepository.existsByProductNoAndMemberNo(request.getProductId(), request.getMemberNo());
    }
}