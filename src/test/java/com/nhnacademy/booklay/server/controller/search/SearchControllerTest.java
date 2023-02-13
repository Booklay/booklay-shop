package com.nhnacademy.booklay.server.controller.search;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.search.request.SearchIdRequest;
import com.nhnacademy.booklay.server.dto.search.request.SearchKeywordsRequest;
import com.nhnacademy.booklay.server.service.search.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(SearchController.class)
@MockBean(JpaMetamodelMappingContext.class)
class SearchControllerTest {

    @MockBean
    SearchService searchService;

    @Autowired
    SearchController searchController;

    @Autowired
    MockMvc mockMvc;

    private SearchKeywordsRequest request;
    private ObjectMapper mapper;


    private static final String URI_PREFIX = "/search";


    @BeforeEach
    void setUp() {

        mapper = new ObjectMapper();
        request = new SearchKeywordsRequest();

    }

    @Test
    void searchAll() throws Exception {

        mockMvc.perform(get(URI_PREFIX + "/products?page=0"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    }

    @Test
    void searchProductsByKeywords() throws Exception {

        ReflectionTestUtils.setField(request,"classification","keywords");
        ReflectionTestUtils.setField(request,"keywords","키워드");

        mockMvc.perform(post(URI_PREFIX + "/products/keywords?page=0")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    }

    @Test
    void searchProductsByCategory() throws Exception {

        SearchIdRequest searchIdRequest = new SearchIdRequest();

        ReflectionTestUtils.setField(searchIdRequest,"classification","categories");
        ReflectionTestUtils.setField(searchIdRequest,"id",1L);

        mockMvc.perform(post(URI_PREFIX + "/products?page=0")
                .content(mapper.writeValueAsString(searchIdRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    }

    @Test
    void saveAll() throws Exception {

        mockMvc.perform(get(URI_PREFIX + "/save/all"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    }
}