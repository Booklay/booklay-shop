package com.nhnacademy.booklay.server.controller.admin.category;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.service.category.CategoryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@WebMvcTest(CategoryAdminController.class)
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class CategoryAdminControllerTest {

    @MockBean
    CategoryService categoryService;

    @Autowired
    CategoryAdminController categoryController;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper;
    Category category;
    CategoryResponse categoryResponse;
    CategoryCreateRequest createDto;
    CategoryUpdateRequest updateDto;

    private static final String IDENTIFIER = "admin/categories";
    private static final String URI_PREFIX = "/" + IDENTIFIER;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(print())
            .alwaysDo(document("{ClassName}/{methodName}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
                )
            )
            .build();


        objectMapper = new ObjectMapper();
        category = Dummy.getDummyCategory();

        categoryResponse = new CategoryResponse(category);

        createDto = new CategoryCreateRequest();

        ReflectionTestUtils.setField(createDto, "id", category.getId());
        ReflectionTestUtils.setField(createDto, "parentCategoryId", category.getParent().getId());
        ReflectionTestUtils.setField(createDto, "name", category.getName());
        ReflectionTestUtils.setField(createDto, "isExposure", category.getIsExposure());

        updateDto = new CategoryUpdateRequest();

        ReflectionTestUtils.setField(updateDto, "id", category.getId());
        ReflectionTestUtils.setField(updateDto, "parentCategoryId", category.getParent().getId());
        ReflectionTestUtils.setField(updateDto, "name", category.getName());
        ReflectionTestUtils.setField(updateDto, "isExposure", category.getIsExposure());

    }

    @Test
    void test_RetrieveCategoryStep() throws Exception {

        mockMvc.perform(get(URI_PREFIX + "/steps/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        then(categoryService).should(times(1)).retrieveCategoryStep(1L);
    }

    @Test
    @DisplayName("카테고리 등록 성공")
    void testRegisterCategory() throws Exception {
        //given

        //then
        mockMvc.perform(post(URI_PREFIX)
                .content(objectMapper.writeValueAsString(createDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();

        then(categoryService).should(times(1)).retrieveCategory(101L);

    }

    @Test
    @DisplayName("카테고리 등록 실패, 입력값 오류")
    void testCreateCategory_ifCreateRequestIncludeNullOrBlank_throwsValidationFailedException()
        throws Exception {
        //given
        CategoryCreateRequest emptyRequest = new CategoryCreateRequest();
        //mocking

        //then
        mockMvc.perform(post(URI_PREFIX)
                .content(objectMapper.writeValueAsString(emptyRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("단일 카테고리 검색 성공")
    void testRetrieveCategory() throws Exception {
        //mocking
        when(categoryService.retrieveCategory(category.getId())).thenReturn(categoryResponse);

        //then
        mockMvc.perform(get(URI_PREFIX + "/" + category.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("단일 카테고리 검색 실패, 존재하지 않는 카테고리 ID")
    void testRetrieveCategory_ifNotExistedCategoryId() throws Exception {
        //mocking
        when(categoryService.retrieveCategory(category.getId())).thenThrow(
            NotFoundException.class);

        //then
        mockMvc.perform(get(URI_PREFIX + "/" + category.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("카테고리 리스트 검색 성공")
    void testRetrieveCategoryListWithPageable() throws Exception {
        //given
        PageResponse<CategoryResponse> page = new PageResponse<CategoryResponse>(
            0, 0, 0, List.of()
        );

        //mocking
        when(categoryService.retrieveCategory(Pageable.unpaged())).thenReturn(page);

        //then
        mockMvc.perform(get(URI_PREFIX))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    void testUpdateCategory() throws Exception {
        //given

        //mocking
        when(categoryService.retrieveCategory(category.getId())).thenReturn(categoryResponse);

        //then
        mockMvc.perform(put(URI_PREFIX + "/" + category.getId())
                .content(objectMapper.writeValueAsString(updateDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    }

    //    @Test
    @DisplayName("카테고리 수정 실패, 입력값 오류")
    void testUpdateCategory_ifUpdateRequestIncludeNullOrBlank_throwsValidationFailedException()
        throws Exception {
        //given
        CategoryUpdateRequest emptyRequest = new CategoryUpdateRequest();

        //when

        //then
        mockMvc.perform(put(URI_PREFIX + "/" + category.getId())
                .content(objectMapper.writeValueAsString(emptyRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("카테고리 삭제 매핑 테스트")
    void testDeleteCategory() throws Exception {
        //given

        mockMvc.perform(delete(URI_PREFIX + "/" + category.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        //then
        verify(categoryService, times(1)).deleteCategory(category.getId());
    }

    @Test
    @DisplayName("카테고리 삭제 매핑 테스트")
    void testDeleteCategory_ifNotExistedCategoryId() throws Exception {
        //mocking

        doThrow(NotFoundException.class).when(categoryService)
            .deleteCategory(category.getId());

        //then
        mockMvc.perform(delete(URI_PREFIX + "/" + category.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andDo(print())
            .andReturn();
    }
}
