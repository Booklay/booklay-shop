package com.nhnacademy.booklay.server.controller.admin.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.service.product.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TagAdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
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

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    updateTagRequest = new UpdateTagRequest(1L, "#test_tag");
    tagNo = 1L;
  }

  @Test
  void tagPage() {
  }

  @Test
  void tagRegister() throws Exception {
    CreateTagRequest createTagRequest = new CreateTagRequest("#test_tag_2");

    mockMvc.perform(post("/admin/tag")
            .content(objectMapper.writeValueAsString(createTagRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  void tagUpdate() throws Exception {
    mockMvc.perform(put("/admin/tag")
            .content(objectMapper.writeValueAsString(updateTagRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted());
  }

  @Test
  void tagDelete() throws Exception {
    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(tagNo);

    mockMvc.perform(delete("/admin/tag")
            .content(objectMapper.writeValueAsString(deleteIdRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void tagProductPage() {
  }

  @Test
  void tagProductConnect() throws Exception {
    CreateDeleteTagProductRequest createRequest = new CreateDeleteTagProductRequest(1L, 1L);

    mockMvc.perform(post("/admin/tag/product")
            .content(objectMapper.writeValueAsString(createRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  void tagProductDisconnect() throws Exception {
    CreateDeleteTagProductRequest deleteRequest = new CreateDeleteTagProductRequest(1L, 1L);

    mockMvc.perform(delete("/admin/tag/product")
            .content(objectMapper.writeValueAsString(deleteRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted());
  }
}