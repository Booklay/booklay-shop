package com.nhnacademy.booklay.server.service.review;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Review;
import com.nhnacademy.booklay.server.repository.ReviewRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import com.nhnacademy.booklay.server.service.storage.FileService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @InjectMocks
  ReviewServiceImpl reviewService;
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
  Review review;
  Long productId;
  Product product;
  Long memberNo;
  Member member;

  @BeforeEach
  void setup() {
    reviewId = 1L;
    productId = 1L;
    memberNo = 1L;

    product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    ReflectionTestUtils.setField(product, "id", productId);
    member = Dummy.getDummyMember();
    ReflectionTestUtils.setField(member, "memberNo", memberNo);

    review = Review.builder()
        .score(1L)
        .productNo(productId)
        .product(product)
        .content("really good product")
        .memberNo(memberNo)
        .member(member)
        .createdAt(LocalDateTime.now())
        .isDeleted(false)
        .build();
  }

  @Test
  void createReview() {

  }

  @Test
  void retrieveReviewListByProductId() {

    Pageable pageable = PageRequest.of(0, 20);
    Page<Review> reviewPage = new PageImpl<>(List.of(), pageable, 0);

    given(reviewRepository.findReviewsByProductNoAndIsDeleted(productId, false,
        pageable)).willReturn(reviewPage);

    List<RetrieveReviewResponse> reviews = new ArrayList<>();

    List<Review> allReviews = List.of();
    given(reviewRepository.findReviewsByProductNoAndIsDeleted(productId, false)).willReturn(
        allReviews);

    AtomicReference<Float> score = new AtomicReference<>((float) 0);
    allReviews.forEach(x -> score.updateAndGet(v -> v + x.getScore()));

    Float scoreAverage = score.get() / allReviews.size();

    reviewPage.getContent().forEach(review -> reviews.add(new RetrieveReviewResponse(review)));

    SearchPageResponse expect = SearchPageResponse.<RetrieveReviewResponse>builder()
        .totalHits(reviewPage.getTotalElements())
        .pageNumber(reviewPage.getNumber())
        .pageSize(reviewPage.getSize())
        .totalPages(reviewPage.getTotalPages())
        .data(reviews)
        .averageScore(String.format("%.1f", scoreAverage))
        .build();

    SearchPageResponse result = reviewService.retrieveReviewListByProductId(productId, pageable);

    assertThat(result.getData().size()).isEqualTo(expect.getData().size());
  }

  @Test
  void retrieveReviewListByReviewId() {
    given(reviewRepository.findById(reviewId)).willReturn(Optional.ofNullable(review));

    RetrieveReviewResponse result = reviewService.retrieveReviewListByReviewId(reviewId);

    RetrieveReviewResponse expect = new RetrieveReviewResponse(review);

    BDDMockito.then(reviewRepository).should().findById(reviewId);
    assertThat(result.getContent()).isEqualTo(expect.getContent());
    assertThat(result.getProductId()).isEqualTo(expect.getProductId());
    assertThat(result.getScore()).isEqualTo(expect.getScore());
    assertThat(result.getId()).isEqualTo(expect.getId());

  }

  @Test
  void deleteReviewById() {
    reviewService.deleteReviewById(reviewId);

    BDDMockito.then(reviewRepository).should().softDeleteReview(reviewId);

  }

}