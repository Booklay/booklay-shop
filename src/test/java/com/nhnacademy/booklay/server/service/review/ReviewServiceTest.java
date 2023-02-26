package com.nhnacademy.booklay.server.service.review;

import com.nhnacademy.booklay.server.repository.ReviewRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import com.nhnacademy.booklay.server.service.storage.FileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @InjectMocks
  ReviewService reviewService;
  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private MemberRepository memberRepository;
  @Mock
  private PointHistoryService pointHistoryService;
  @Mock
  private FileService fileService;

  Long reviewId;

  @BeforeEach
  void setup() {
    reviewId = 1L;
  }
  @Test
  void createReview() {

  }

  @Test
  void retrieveReviewListByProductId() {
  }

  @Test
  void retrieveReviewListByReviewId() {
  }

  @Test
  void deleteReviewById() {
    reviewService.deleteReviewById(reviewId);

    BDDMockito.then(reviewRepository).should().softDeleteReview(reviewId);

  }

}