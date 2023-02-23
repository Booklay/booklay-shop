package com.nhnacademy.booklay.server.controller.board;

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
import com.nhnacademy.booklay.server.dto.board.request.CommentRequest;
import com.nhnacademy.booklay.server.dto.board.response.CommentResponse;
import com.nhnacademy.booklay.server.service.board.CommentService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@WebMvcTest(CommentController.class)
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {

  @Autowired
  CommentController commentController;
  @MockBean
  CommentService commentService;
  @Autowired
  MockMvc mockMvc;

  ObjectMapper objectMapper;
  CommentRequest request;

  private final String URI_PREFIX = "/comment";

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(print())
        .alwaysDo(document("board/comment/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            )
        )
        .build();

    objectMapper = new ObjectMapper();

    request = new CommentRequest(1L, 1L, 1L, "content", 1L, 0, 0);

  }

  @Test
  void retrieveCommentPage() throws Exception {
    Long postId = 1L;
    Pageable pageable = PageRequest.of(0, 20);
    Page<CommentResponse> page = new PageImpl<>(List.of(), pageable, 0);

    when(commentService.retrieveCommentPage(postId, pageable)).thenReturn(page);
    mockMvc.perform(get(URI_PREFIX + "/" + postId))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(commentService).should(times(1)).retrieveCommentPage(any(), any());
  }

  @Test
  void createComment() throws Exception {
    when(commentService.createComment(request)).thenReturn(1L);

    mockMvc.perform(post(URI_PREFIX).content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    BDDMockito.then(commentService).should().createComment(any());
  }

  @Test
  void updateComment() throws Exception {
    when(commentService.updateComment(request)).thenReturn(1L);

    mockMvc.perform(put(URI_PREFIX).content(objectMapper.writeValueAsString(request)).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    then(commentService).should(times(1)).updateComment(any());
  }

  @Test
  void deleteComment() throws Exception {
    Long commentId = 1L;

    mockMvc.perform(delete(URI_PREFIX + "/" + commentId))
        .andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    then(commentService).should(times(1)).deleteComment(any(), any());
  }
}