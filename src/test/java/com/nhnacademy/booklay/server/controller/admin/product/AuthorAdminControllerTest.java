package com.nhnacademy.booklay.server.controller.admin.product;

import static org.hamcrest.Matchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    author = new RetrieveAuthorResponse(1L, "test");
    authorNo = 1L;
    updateAuthorRequest = new UpdateAuthorRequest(authorNo, "test2");
  }

  @Test
  void authorPage() throws Exception {
    //given
    Pageable pageable = Pageable.ofSize(10);
    List<RetrieveAuthorResponse> content = new ArrayList<>();
    content.add(author);
    Page<RetrieveAuthorResponse> responses = new PageImpl(content, pageable, 1);
    given(authorService.retrieveAllAuthor(pageable)).willReturn(responses);

    ResultActions result = mockMvc.perform(get("/admin/author").accept(MediaType.APPLICATION_JSON));
//    result.andExpect(status().isOk());
  }

  @Test
  void authorEdit() throws Exception {
    //given
    given(authorService.retrieveAuthorForUpdate(authorNo)).willReturn(author);

    //when
    ResultActions result = mockMvc.perform(get("/admin/author/" + authorNo)
        .accept(MediaType.APPLICATION_JSON));

    //then
    result.andExpect(status().isOk());
  }

  @Test
  void authorRegister() throws Exception {
    CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest("create");
    given(authorService.createAuthor(createAuthorRequest)).willReturn(authorNo);

    mockMvc.perform(post("/admin/author")
            .content(objectMapper.writeValueAsString(createAuthorRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  void authorUpdate() throws Exception {
    given(authorService.updateAuthor(updateAuthorRequest)).willReturn(authorNo);

    //then
    mockMvc.perform(put("/admin/author")
            .content(objectMapper.writeValueAsString(updateAuthorRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted());
  }

  @Test
  void authorDelete() throws Exception {
    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(authorNo);

    mockMvc.perform(delete("/admin/author")
            .content(objectMapper.writeValueAsString(deleteIdRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}