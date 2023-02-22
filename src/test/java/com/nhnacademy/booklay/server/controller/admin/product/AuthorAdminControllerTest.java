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
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.service.product.AuthorService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(AuthorAdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AuthorAdminControllerTest {

  @MockBean
  AuthorService authorService;
  @Autowired
  AuthorAdminController authorAdminController;
  @Autowired
  MockMvc mockMvc;
  ObjectMapper objectMapper;
  RetrieveAuthorResponse author;
  Long authorNo;
  UpdateAuthorRequest updateAuthorRequest;

  private final String URI_PRE_FIX = "/admin/author";

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(print())
        .alwaysDo(document("admin/product/author/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            )
        )
        .build();

    objectMapper = new ObjectMapper();
    author = new RetrieveAuthorResponse(1L, "test");
    authorNo = 1L;
    updateAuthorRequest = new UpdateAuthorRequest(authorNo, "test2");
  }

  @Test
  void authorPage() throws Exception {
    //given
    Pageable pageable = PageRequest.of(0, 20);
    Page<RetrieveAuthorResponse> page = new PageImpl(List.of(), pageable, 0);

    when(authorService.retrieveAllAuthor(pageable)).thenReturn(page);

    //when
    mockMvc.perform(get(URI_PRE_FIX).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(authorService).should(times(1)).retrieveAllAuthor(any());
  }

  @Test
  void authorEdit() throws Exception {
    //given
    when(authorService.retrieveAuthorForUpdate(authorNo)).thenReturn(author);

    //when
    mockMvc.perform(get(URI_PRE_FIX + "/"+authorNo)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(authorService).should(times(1)).retrieveAuthorForUpdate(any());
  }

  @Test
  void authorRegister() throws Exception {
    CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest("create");

    mockMvc.perform(post(URI_PRE_FIX)
            .content(objectMapper.writeValueAsString(createAuthorRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print())
        .andReturn();

    then(authorService).should(times(1)).createAuthor(any());
  }

  @Test
  void authorUpdate() throws Exception {
    //then
    mockMvc.perform(put(URI_PRE_FIX)
            .content(objectMapper.writeValueAsString(updateAuthorRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andDo(print())
        .andReturn();

    then(authorService).should(times(1)).updateAuthor(any());
  }

  @Test
  void authorDelete() throws Exception {
    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(authorNo);

    mockMvc.perform(delete(URI_PRE_FIX)
            .content(objectMapper.writeValueAsString(deleteIdRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    then(authorService).should(times(1)).deleteAuthor(any());
  }
}