package com.nhnacademy.booklay.server.controller.review;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.review.request.ReviewCreateRequest;
import com.nhnacademy.booklay.server.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Review;
import com.nhnacademy.booklay.server.exception.controller.CreateFailedException;
import com.nhnacademy.booklay.server.service.mypage.RestockingNotificationService;
import com.nhnacademy.booklay.server.service.review.ReviewServiceImpl;
import com.nhnacademy.booklay.server.service.review.cache.ReviewResponsePageCacheWrapService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ReviewControllerTest {

  @MockBean
  ReviewServiceImpl reviewService;
  @MockBean
  RestockingNotificationService restockingNotificationService;
  @MockBean
  ReviewResponsePageCacheWrapService reviewResponsePageCacheWrapService;
  @Autowired
  ReviewController reviewController;
  @Autowired
  MockMvc mockMvc;
  ObjectMapper objectMapper;

  Long reviewId;
  Long productId;
  ReviewCreateRequest reviewCreateRequest;
  RetrieveReviewResponse reviewResponse;
  SearchPageResponse searchResponse;
  MockMultipartFile file;

  private final String URI_PRE_FIX = "/reviews";

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(print())
        .alwaysDo(document("review/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            )
        )
        .build();

    objectMapper = new ObjectMapper();
    reviewId = 1L;
    productId = 1L;

    reviewCreateRequest = new ReviewCreateRequest();
    ReflectionTestUtils.setField(reviewCreateRequest, "productId", 1L);
    ReflectionTestUtils.setField(reviewCreateRequest, "score", 5L);
    ReflectionTestUtils.setField(reviewCreateRequest, "content",
        "test review content this books is really good");
    ReflectionTestUtils.setField(reviewCreateRequest, "memberNo", 1L);

    reviewResponse = new RetrieveReviewResponse(
        new Review(1L, DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto()), 1L,
            Dummy.getDummyMember(), 1L, DummyCart.getDummyFile(), 1L, 1L,
            "dummy content", false, LocalDateTime.now(), LocalDateTime.now()));

    searchResponse = SearchPageResponse.builder()
        .searchKeywords("keyword")
        .pageSize(1)
        .totalHits(1L)
        .totalPages(1)
        .averageScore("0.1")
        .pageNumber(1)
        .data(List.of())
        .build();

    file = new MockMultipartFile(
        "file",
        "file.txt",
        "test/txt",
        "multipart".getBytes());

  }

  @Test
  void createReview() throws Exception {

    MockMultipartFile request = new MockMultipartFile(
        "request",
        "file.txt",
        String.valueOf(MediaType.APPLICATION_JSON),
        objectMapper.writeValueAsString(reviewCreateRequest).getBytes());

    mockMvc.perform(multipart(URI_PRE_FIX)
            .file(file)
            .file(request))
        .andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    then(reviewService).should(times(1)).createReview(any(), any());
  }

  @Test
  void createReview_throwsCreateFailed() throws Exception {

    // given
    doThrow(CreateFailedException.class).when(reviewService).createReview(any(), any());
    MockMultipartFile request = new MockMultipartFile(
        "request",
        "file.txt",
        String.valueOf(MediaType.APPLICATION_JSON),
        objectMapper.writeValueAsString(reviewCreateRequest).getBytes());

    // when, then
    mockMvc.perform(multipart(URI_PRE_FIX)
            .file(file)
            .file(request))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(reviewService).should(times(1)).createReview(any(), any());
  }

  @Test
  void retrieveReviewByProductId_bodyNonNull() throws Exception {

    Pageable pageable = PageRequest.of(0, 20);
//    when(reviewService.retrieveReviewListByProductId(productId, pageable)).thenReturn(
//        searchResponse);
    when(reviewResponsePageCacheWrapService.cacheRetrievePostResponsePage(productId, pageable)).thenReturn(
        searchResponse);

    mockMvc.perform(get(URI_PRE_FIX + "/products/" + productId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andDo(print())
        .andReturn();

//    then(reviewService).should(times(1)).retrieveReviewListByProductId(any(), any());
    then(reviewResponsePageCacheWrapService).should(times(1)).cacheRetrievePostResponsePage(any(), any());
  }

  @Test
  void retrieveReviewByProductId_bodyNull() throws Exception {

    Pageable pageable = PageRequest.of(0, 20);
    when(reviewResponsePageCacheWrapService.cacheRetrievePostResponsePage(productId, pageable)).thenReturn(
        null);

    mockMvc.perform(get(URI_PRE_FIX + "/products/" + productId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(reviewResponsePageCacheWrapService).should(times(1)).cacheRetrievePostResponsePage(any(), any());
  }

  @Test
  void retrieveReviewByReviewId() throws Exception {
    Pageable pageable = PageRequest.of(0, 20);
    Page<RetrieveProductResponse> page = new PageImpl(List.of(), pageable, 0);
    when(reviewService.retrieveReviewListByReviewId(reviewId)).thenReturn(reviewResponse);

    mockMvc.perform(get(URI_PRE_FIX + "/" + reviewId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(reviewService).should(times(1)).retrieveReviewListByReviewId(any());
  }

  @Test
  void deleteReviewByReviewId_success() throws Exception {
    when(reviewService.deleteReviewById(reviewId)).thenReturn(true);

    mockMvc.perform(delete(URI_PRE_FIX + "/" + reviewId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(reviewService).should(times(1)).deleteReviewById(any());
  }

  @Test
  void deleteReviewByReviewId_failure() throws Exception {
    when(reviewService.deleteReviewById(reviewId)).thenReturn(false);

    mockMvc.perform(delete(URI_PRE_FIX + "/" + reviewId))
        .andExpect(status().isNotAcceptable())
        .andDo(print())
        .andReturn();

    then(reviewService).should(times(1)).deleteReviewById(any());
  }
}