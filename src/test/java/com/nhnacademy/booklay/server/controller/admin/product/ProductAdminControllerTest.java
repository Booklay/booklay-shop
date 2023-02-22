package com.nhnacademy.booklay.server.controller.admin.product;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteProductRelationRequest;
import com.nhnacademy.booklay.server.dto.product.request.DisAndConnectBookWithSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.product.BookSubscribeService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.time.LocalDate;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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

  DisAndConnectBookWithSubscribeRequest subscribeRequest;
  CreateDeleteProductRelationRequest relationRequest;
  private final String URI_PRE_FIX = "/admin/product";

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(print())
        .alwaysDo(document("admin/author/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            )
        )
        .build();

    objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    pageable = PageRequest.of(0, 20);
    productId = 1L;

    subscribeRequest = new DisAndConnectBookWithSubscribeRequest();
    ReflectionTestUtils.setField(subscribeRequest, "productId", 1L);
    ReflectionTestUtils.setField(subscribeRequest, "subscribeId", 2L);
    ReflectionTestUtils.setField(subscribeRequest, "releaseDate", LocalDate.of(2022, 12, 26));

    relationRequest = new CreateDeleteProductRelationRequest();
    ReflectionTestUtils.setField(relationRequest, "baseId", 1L);
    ReflectionTestUtils.setField(relationRequest, "targetId", 2L);
  }

  @Test
  void adminProduct() throws Exception {
    Page<RetrieveProductResponse> page = new PageImpl<>(List.of(), pageable, 0);
    when(productService.retrieveAdminProductPage(pageable)).thenReturn(page);

    mockMvc.perform(get(URI_PRE_FIX).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

  }

//  @Test
//  void postBookRegister() throws Exception {
//    MockMultipartFile image = new MockMultipartFile("name", "stream".getBytes());
//
//    mockMvc.perform(multipart(URI_PRE_FIX)
//            .file(image)
//            .content(objectMapper.writeValueAsString(bookCreateRequest))
//            .contentType(MediaType.MULTIPART_FORM_DATA))
//        .andExpect(status().isCreated())
//        .andDo(print())
//        .andReturn();
//
//  }

  @Test
  void getBookData() throws Exception {
    mockMvc.perform(get(URI_PRE_FIX + "/books/" + productId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

//  @Test
//  void postBookUpdater() throws Exception {
//    mockMvc.perform(put(URI_PRE_FIX + "/books/" + productId)
//            .content(objectMapper.writeValueAsString(updateAuthorRequest))
//            .contentType(MediaType.APPLICATION_JSON))
//        .andExpect(status().isAccepted())
//        .andDo(print())
//        .andReturn();
//  }

//  @Test
//  void postSubscribeRegister() throws Exception {
//    mockMvc.perform(post(URI_PRE_FIX)
//            .content(objectMapper.writeValueAsString(createAuthorRequest))
//            .contentType(MediaType.APPLICATION_JSON))
//        .andExpect(status().isCreated())
//        .andDo(print())
//        .andReturn();
//  }

  @Test
  void getSubscribeData() throws Exception {
    mockMvc.perform(
            get(URI_PRE_FIX + "/subscribes/" + productId).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

//  @Test
//  void postSubscribeUpdater() throws Exception {
//    mockMvc.perform(put(URI_PRE_FIX)
//            .content(objectMapper.writeValueAsString(updateAuthorRequest))
//            .contentType(MediaType.APPLICATION_JSON))
//        .andExpect(status().isAccepted())
//        .andDo(print())
//        .andReturn();
//  }

  @Test
  void getBooksDataForSubscribe() throws Exception {
    Page<RetrieveBookForSubscribeResponse> page = new PageImpl<>(List.of(), pageable, 0);
    when(productService.retrieveBookDataForSubscribe(pageable, subscribeId)).thenReturn(page);
    PageResponse<RetrieveBookForSubscribeResponse> body = new PageResponse<>(page);

    mockMvc.perform(get(URI_PRE_FIX + "/subscribes/connect/" + subscribeId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  void booksAndSubscribeConnect() throws Exception {
    mockMvc.perform(post(URI_PRE_FIX + "/subscribes/connect/" + subscribeId)
            .content(objectMapper.writeValueAsString(subscribeRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  void booksAndSubscribeDisconnect() throws Exception {
    mockMvc.perform(delete(URI_PRE_FIX + "/subscribes/connect/" + subscribeId)
            .content(objectMapper.writeValueAsString(subscribeRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  void retrieveRecommendConnector() throws Exception {
    Page<RetrieveProductResponse> page = new PageImpl<>(List.of(), pageable, 0);

    when(productRelationService.retrieveRecommendConnection(productId, pageable)).thenReturn(page);

    mockMvc.perform(get(URI_PRE_FIX + "/recommend/" + productId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  void createRecommend() throws Exception {
    mockMvc.perform(post(URI_PRE_FIX + "/recommend")
            .content(objectMapper.writeValueAsString(relationRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  void deleteRecommend() throws Exception {
    mockMvc.perform(delete(URI_PRE_FIX + "/recommend")
            .content(objectMapper.writeValueAsString(relationRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  void softDeleteProduct() throws Exception {
    mockMvc.perform(delete(URI_PRE_FIX + "/" + productId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }
}