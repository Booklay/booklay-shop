package com.nhnacademy.booklay.server.controller.admin.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteProductRelationRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.DisAndConnectBookWithSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.service.product.BookSubscribeService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@WebMvcTest(ProductAdminController.class)
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductAdminControllerTest {

  @MockBean
  ProductService productService;
  @MockBean
  BookSubscribeService bookSubscribeService;
  @MockBean
  ProductRelationService productRelationService;

  @Autowired
  ProductAdminController productAdminController;
  @Autowired
  MockMvc mockMvc;

  ObjectMapper objectMapper;

  Pageable pageable;
  Long productId;
  Long subscribeId;
  CreateUpdateProductBookRequest createUpdateProductBookRequest;

  DisAndConnectBookWithSubscribeRequest subscribeRequest;
  CreateDeleteProductRelationRequest relationRequest;
  private final String URI_PRE_FIX = "/admin/product";

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(print())
        .alwaysDo(document("admin/product/product/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            )
        )
        .build();

    objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    pageable = PageRequest.of(0, 20);
    productId = 1L;
    subscribeId = 1L;

    createUpdateProductBookRequest = DummyCart.getDummyProductBookDto();

    subscribeRequest = new DisAndConnectBookWithSubscribeRequest();
    ReflectionTestUtils.setField(subscribeRequest, "productId", 1L);
    ReflectionTestUtils.setField(subscribeRequest, "subscribeId", 2L);
    ReflectionTestUtils.setField(subscribeRequest, "releaseDate", LocalDate.of(2022, 12, 26));

    relationRequest = new CreateDeleteProductRelationRequest();
    ReflectionTestUtils.setField(relationRequest, "baseId", 1L);
    ReflectionTestUtils.setField(relationRequest, "targetId", 2L);
  }

  @Test
  @DisplayName("관리자 페이지 상품 목록 호출")
  void adminProduct() throws Exception {
    Page<RetrieveProductResponse> page = new PageImpl<>(List.of(), pageable, 0);
    when(productService.retrieveAdminProductPage(pageable)).thenReturn(page);

    mockMvc.perform(get(URI_PRE_FIX).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(productService).should(times(1)).retrieveAdminProductPage(any());

  }

  @Test
  @DisplayName("책 상품 수정을 위한 조회 성공")
  void getBookData() throws Exception {
    mockMvc.perform(get(URI_PRE_FIX + "/books/" + productId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(productService).should(times(1)).retrieveBookData(any());
  }

  @Test
  @DisplayName("구독 상품 수정을 위한 조회 성공")
  void getSubscribeData() throws Exception {
    mockMvc.perform(
            get(URI_PRE_FIX + "/subscribes/" + productId).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(productService).should(times(1)).retrieveBookData(any());
  }

  @Test
  @DisplayName("상품-구독 테이블 연동을 위한 페이지 조회")
  void getBooksDataForSubscribe() throws Exception {
    Page<RetrieveBookForSubscribeResponse> page = new PageImpl<>(List.of(), pageable, 0);
    when(productService.retrieveBookDataForSubscribe(pageable, subscribeId)).thenReturn(page);

    mockMvc.perform(get(URI_PRE_FIX + "/subscribes/connect/" + subscribeId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(productService).should(times(1)).retrieveBookDataForSubscribe(any(), any());
  }

  @Test
  @DisplayName("상품-구독 조인 테이블 등록")
  void booksAndSubscribeConnect() throws Exception {
    mockMvc.perform(post(URI_PRE_FIX + "/subscribes/connect/" + subscribeId)
            .content(objectMapper.writeValueAsString(subscribeRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(bookSubscribeService).should(times(1)).bookSubscribeConnection(any());
  }

  @Test
  @DisplayName("상품-구독 조인 테이블 삭제")
  void booksAndSubscribeDisconnect() throws Exception {
    mockMvc.perform(delete(URI_PRE_FIX + "/subscribes/connect/" + subscribeId)
            .content(objectMapper.writeValueAsString(subscribeRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(bookSubscribeService).should(times(1)).bookSubscribeDisconnection(any());
  }

  @Test
  @DisplayName("연관상품 등록 위한 상품 목록 호출")
  void retrieveRecommendConnector() throws Exception {
    Page<RetrieveProductResponse> page = new PageImpl<>(List.of(), pageable, 0);

    when(productRelationService.retrieveRecommendConnection(productId, pageable)).thenReturn(page);

    mockMvc.perform(get(URI_PRE_FIX + "/recommend/" + productId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(productRelationService).should(times(1)).retrieveRecommendConnection(any(), any());
  }

  @Test
  @DisplayName("연관상품 등록")
  void createRecommend() throws Exception {
    mockMvc.perform(post(URI_PRE_FIX + "/recommend")
            .content(objectMapper.writeValueAsString(relationRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(productRelationService).should(times(1)).createProductRelation(any());
  }

  @Test
  @DisplayName("연관 상품 삭제")
  void deleteRecommend() throws Exception {
    mockMvc.perform(delete(URI_PRE_FIX + "/recommend")
            .content(objectMapper.writeValueAsString(relationRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(productRelationService).should(times(1)).deleteProductRelation(any());
  }

  @Test
  @DisplayName("상품 소프트 딜리트")
  void softDeleteProduct() throws Exception {
    mockMvc.perform(delete(URI_PRE_FIX + "/" + productId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(productService).should(times(1)).softDelete(any());
  }
}