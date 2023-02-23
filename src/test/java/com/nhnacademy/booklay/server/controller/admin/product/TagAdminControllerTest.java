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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.TagProductResponse;
import com.nhnacademy.booklay.server.service.product.TagService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(TagAdminController.class)
class TagAdminControllerTest {

  @MockBean
  TagService tagService;

  @Autowired
  TagAdminController tagAdminController;
  @Autowired
  MockMvc mockMvc;

  ObjectMapper objectMapper;
  UpdateTagRequest updateTagRequest;
  Long tagNo;
  Pageable pageable;
  private final String URI_PRE_FIX = "/admin/tag";

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(print())
        .alwaysDo(document("admin/tag/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            )
        )
        .build();
    objectMapper = new ObjectMapper();
    pageable = PageRequest.of(0, 20);
    updateTagRequest = new UpdateTagRequest(1L, "#test_tag");
    tagNo = 1L;
  }

  @Test
  void tagPage() throws Exception {

    Page<RetrieveTagResponse> page = new PageImpl<>(List.of(), pageable, 0);
    when(tagService.retrieveAllTag(pageable)).thenReturn(page);

    mockMvc.perform(get(URI_PRE_FIX))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(tagService).should(times(1)).retrieveAllTag(any());
  }

  @Test
  void tagRegister() throws Exception {
    CreateTagRequest createTagRequest = new CreateTagRequest("#test_tag_2");

    mockMvc.perform(post(URI_PRE_FIX)
            .content(objectMapper.writeValueAsString(createTagRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    then(tagService).should(times(1)).createTag(any());
  }

  @Test
  void tagUpdate() throws Exception {
    mockMvc.perform(put(URI_PRE_FIX)
            .content(objectMapper.writeValueAsString(updateTagRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andDo(print())
        .andReturn();

    then(tagService).should(times(1)).updateTag(any());
  }

  @Test
  void tagDelete() throws Exception {
    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(tagNo);

    mockMvc.perform(delete(URI_PRE_FIX)
            .content(objectMapper.writeValueAsString(deleteIdRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(tagService).should(times(1)).deleteTag(any());
  }

  @Test
  void tagProductPage() throws Exception {
    Long productId = 1L;
    Page<TagProductResponse> page = new PageImpl<>(List.of(), pageable, 0);

    when(tagService.retrieveAllTagWithBoolean(pageable, productId)).thenReturn(page);

    mockMvc.perform(get(URI_PRE_FIX + "/product/" + productId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(tagService).should(times(1)).retrieveAllTagWithBoolean(any(),any());
  }

  @Test
  void tagProductConnect() throws Exception {
    CreateDeleteTagProductRequest createRequest = new CreateDeleteTagProductRequest(1L, 1L);

    mockMvc.perform(post(URI_PRE_FIX + "/product")
            .content(objectMapper.writeValueAsString(createRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    then(tagService).should(times(1)).createTagProduct(any());
  }

  @Test
  void tagProductDisconnect() throws Exception {
    CreateDeleteTagProductRequest deleteRequest = new CreateDeleteTagProductRequest(1L, 1L);

    mockMvc.perform(delete(URI_PRE_FIX + "/product")
            .content(objectMapper.writeValueAsString(deleteRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andDo(print())
        .andReturn();

    then(tagService).should(times(1)).deleteTagProduct(any());
  }


  @Test
  void retrieveAllTagList() throws Exception {
    String name = "test_tag";

    when(tagService.tagNameChecker(name)).thenReturn(true);

    //when
    mockMvc.perform(get(URI_PRE_FIX + "/exist/" + name))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(tagService).should(times(1)).tagNameChecker(any());
  }
}