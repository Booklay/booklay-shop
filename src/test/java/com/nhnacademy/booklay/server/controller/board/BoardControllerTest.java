package com.nhnacademy.booklay.server.controller.board;

import static org.mockito.ArgumentMatchers.any;
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
import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.server.dto.board.request.BoardPostUpdateRequest;
import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.service.board.PostService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@WebMvcTest(BoardController.class)
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class BoardControllerTest {

  @Autowired
  BoardController boardController;
  @MockBean
  PostService postService;
  @Autowired
  MockMvc mockMvc;

  ObjectMapper objectMapper;
  BoardPostCreateRequest createRequest;
  BoardPostUpdateRequest updateRequest;
  PostResponse postResponse;
  private static final String URI_PREFIX = "/board";

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(print())
        .alwaysDo(document("board/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            )
        )
        .build();

    objectMapper = new ObjectMapper();

    createRequest = new BoardPostCreateRequest(1, 1L, null, null,
        null, null, "title", "content", true, null);

    updateRequest = new BoardPostUpdateRequest(1L, "update title", "update content", true);

    postResponse = new PostResponse(DummyCart.getDummyPost());
  }

  @Test
  void createPost() throws Exception {
    when(postService.createPost(createRequest)).thenReturn(1L);

    mockMvc.perform(post(URI_PREFIX).content(objectMapper.writeValueAsString(createRequest))
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    BDDMockito.then(postService).should().createPost(any());
  }

  @Test
  void updatePost() throws Exception {
    when(postService.retrievePostById(updateRequest.getPostId())).thenReturn(postResponse);

    mockMvc.perform(put(URI_PREFIX).content(objectMapper.writeValueAsString(updateRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andDo(print())
        .andReturn();


  }

  @Test
  void retrieveProductQNA() throws Exception {
    Page<PostResponse> page = new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);

    //mocking
    when(postService.retrieveProductQNA(1L, PageRequest.of(0, 20))).thenReturn(page);
    PageResponse<PostResponse> body = new PageResponse<>(page);

    mockMvc.perform(get(URI_PREFIX + "/product/" + 1))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

  }

  @Test
  void retrieveNotice() throws Exception {
    Page<PostResponse> page = new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);

    //mocking
    when(postService.retrieveNotice(PageRequest.of(0, 20))).thenReturn(page);
    PageResponse<PostResponse> result = new PageResponse<>(page);

    mockMvc.perform(get(URI_PREFIX + "/notice"))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

  }

  @Test
  void retrieveNoticeListForAdmin() throws Exception {
    List<PostResponse> response = new ArrayList<>();
    Integer limit = 5;

    when(postService.retrieveNoticeList(limit)).thenReturn(response);

    mockMvc.perform(
            get(URI_PREFIX + "/notice/" + limit))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  void viewPost() throws Exception {
    Long postId = 1L;
    when(postService.retrievePostById(postId)).thenReturn(postResponse);

    mockMvc.perform(get(URI_PREFIX + "/" + postId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  void confirmAnswer() throws Exception {
    Long postId = 1L;
    when(postService.updateConfirmAnswer(postId)).thenReturn(postId);

    mockMvc.perform(
            put(URI_PREFIX + "/confirm/" + postId))
        .andExpect(status().isAccepted())
        .andDo(print())
        .andReturn();
  }

  @Test
  void deletePost() throws Exception {
    Long postId = 1L;

    mockMvc.perform(
            delete(URI_PREFIX + "/" + postId))
        .andExpect(status().isAccepted())
        .andDo(print())
        .andReturn();
  }
}