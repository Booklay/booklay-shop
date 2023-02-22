package com.nhnacademy.booklay.server.controller.mypage;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.product.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.mypage.RestockingNotificationService;
import com.nhnacademy.booklay.server.service.mypage.WishlistService;
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
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(MyPageProductController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MyPageProductControllerTest {

  @MockBean
  WishlistService wishlistService;
  @MockBean
  RestockingNotificationService restockingNotificationService;
  @Autowired
  MyPageProductController myPageProductController;
  @Autowired
  MockMvc mockMvc;
  ObjectMapper objectMapper;
  Long memberNo;
  WishlistAndAlarmRequest request;
  private final String URI_PRE_FIX = "/mypage/product";

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(print())
        .alwaysDo(document("mypage/product/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            )
        )
        .build();

    objectMapper = new ObjectMapper();

    memberNo = 1L;
    request = new WishlistAndAlarmRequest();
    ReflectionTestUtils.setField(request, "memberNo", 1L);
    ReflectionTestUtils.setField(request, "productId", 1L);
  }

  @Test
  void retrieveWishlist() throws Exception {
    Pageable pageable = PageRequest.of(0, 20);
    Page<RetrieveProductResponse> page = new PageImpl<>(List.of(), pageable, 0);

    when(wishlistService.retrievePage(memberNo, pageable)).thenReturn(page);

    mockMvc.perform(get(URI_PRE_FIX + "/wishlist/" + memberNo)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(wishlistService).should(times(1)).retrievePage(any(), any());
  }

  @Test
  void retrieveIndexWishlist() throws Exception {
    mockMvc.perform(get(URI_PRE_FIX + "/index/wishlist/" + memberNo)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(wishlistService).should(times(1)).retrieveWishlist(any());
  }

  @Test
  void createWishlist() throws Exception {
    mockMvc.perform(post(URI_PRE_FIX + "/wishlist")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    then(wishlistService).should(times(1)).createWishlist(any());
  }

  @Test
  void retrieveNotification() throws Exception {
    Pageable pageable = PageRequest.of(0, 20);
    Page<RetrieveProductResponse> page = new PageImpl(List.of(), pageable, 0);
    when(restockingNotificationService.retrievePage(memberNo, pageable)).thenReturn(page);

    mockMvc.perform(get(URI_PRE_FIX + "/alarm/" + memberNo)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(restockingNotificationService).should(times(1)).retrievePage(any(), any());
  }

  @Test
  void deleteWishlist() throws Exception {
    mockMvc.perform(delete(URI_PRE_FIX + "/wishlist")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andDo(print())
        .andReturn();

    then(wishlistService).should(times(1)).deleteWishlist(any());
  }

  @Test
  void createAlarm() throws Exception {
    mockMvc.perform(post(URI_PRE_FIX + "/alarm")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    then(restockingNotificationService).should(times(1)).createAlarm(any());
  }

  @Test
  void deleteAlarm() throws Exception {
    mockMvc.perform(delete(URI_PRE_FIX + "/alarm")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andDo(print())
        .andReturn();

    then(restockingNotificationService).should(times(1)).deleteAlarm(any());
  }

  @Test
  void retrieveMemberProduct() throws Exception {
    mockMvc.perform(post(URI_PRE_FIX + "/boolean")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(wishlistService).should(times(1)).retrieveExists(any());
  }
}