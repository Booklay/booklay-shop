package com.nhnacademy.booklay.server.controller.search;

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
import com.nhnacademy.booklay.server.controller.admin.product.TagAdminController;
import com.nhnacademy.booklay.server.dto.search.request.SearchIdRequest;
import com.nhnacademy.booklay.server.dto.search.request.SearchKeywordsRequest;
import com.nhnacademy.booklay.server.dto.search.response.SearchProductResponse;
import com.nhnacademy.booklay.server.service.search.SearchService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(SearchController.class)
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
    void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(print())
            .alwaysDo(document("search/{methodName}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
                )
            )
            .build();

        mapper = new ObjectMapper();
        request = new SearchKeywordsRequest();

    }

    @Test
    void searchAll() throws Exception {

        mockMvc.perform(get(URI_PREFIX + "/products?page=0"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        then(searchService).should(times(1)).getAllProducts(any());
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

        then(searchService).should(times(1)).searchProductsByKeywords(any(), any());
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

        then(searchService).should(times(1)).searchProductsByCategory(any(), any());
    }

    @Test
    void saveAll() throws Exception {

        mockMvc.perform(get(URI_PREFIX + "/save/all"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        then(searchService).should(times(1)).saveAllDocuments();
    }

    @Test
    void searchRecently() throws Exception {
        when(searchService.getLatestProducts()).thenReturn(List.of());

        mockMvc.perform(get(URI_PREFIX + "/products/latest"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        then(searchService).should(times(1)).getLatestProducts();
    }
}