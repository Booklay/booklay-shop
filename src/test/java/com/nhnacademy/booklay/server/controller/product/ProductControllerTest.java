package com.nhnacademy.booklay.server.controller.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.search.request.SearchIdRequest;
import com.nhnacademy.booklay.server.service.product.BookSubscribeService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@WebMvcTest(ProductController.class)
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductControllerTest {

  @Autowired
  ProductController productController;
  @MockBean
  ProductService productService;
  @MockBean
  BookSubscribeService bookSubscribeService;
  @MockBean
  ProductRelationService productRelationService;
  @Autowired
  MockMvc mockMvc;

  ObjectMapper objectMapper;
  Long productId;
  SearchIdRequest request;
  private static final String URI_PREFIX = "/product";

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {

    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(print())
        .alwaysDo(document("product/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            )
        )
        .build();

    objectMapper = new ObjectMapper();

    productId = 1L;

    request = new SearchIdRequest();
    ReflectionTestUtils.setField(request, "classification", "keywords");
    ReflectionTestUtils.setField(request, "id", 1L);
  }

  @Test
  @DisplayName("상품 전체 페이지 조회")
  void getProductPage() throws Exception {
    Pageable pageable = PageRequest.of(0, 20);
    Page<ProductAllInOneResponse> page = new PageImpl(List.of(), pageable, 0);

    when(productService.getProductsPage(pageable)).thenReturn(page);

    mockMvc.perform(get(URI_PREFIX))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(productService).should(times(1)).getProductsPage(any());
  }

  @Test
  @DisplayName("상품 상세 보기")
  void retrieveDetailView() throws Exception {
    mockMvc.perform(get(URI_PREFIX + "/view/" + productId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  @DisplayName("구독 하위 상품 목록 조회")
  void retrieveSubscribedBooks() throws Exception {
    mockMvc.perform(get(URI_PREFIX + "/view/subscribe/" + productId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  @DisplayName("연관 상품 목록 조회")
  void retrieveRecommendProducts() throws Exception {
    mockMvc.perform(get(URI_PREFIX + "/recommend/" + productId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  @DisplayName("상품 전체 목록")
  void searchAll() throws Exception {
    mockMvc.perform(get(URI_PREFIX + "/all"))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  @DisplayName("최근 등록 상품 조회")
  void getLatestProduct() throws Exception {
    mockMvc.perform(get(URI_PREFIX + "/latest"))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  @DisplayName("상품 검색")
  void searchByRequest() throws Exception {
    mockMvc.perform(post(URI_PREFIX + "/request").content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }
}