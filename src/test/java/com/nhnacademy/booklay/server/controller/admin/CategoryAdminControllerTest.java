package com.nhnacademy.booklay.server.controller.admin;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.category.CategoryNotFoundException;
import com.nhnacademy.booklay.server.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryAdminController.class)
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

    private static final String URI_PREFIX = "/admin/categories";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        category = Dummy.getDummyCategory();

        categoryResponse = new CategoryResponse();

        ReflectionTestUtils.setField(categoryResponse, "id", category.getId());
        ReflectionTestUtils.setField(categoryResponse, "parentCategoryId",
            category.getParent().getId());
        ReflectionTestUtils.setField(categoryResponse, "name", category.getName());
        ReflectionTestUtils.setField(categoryResponse, "isExposure", category.getIsExposure());

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
    @DisplayName("카테고리 등록 성공")
    void testRegisterCategory() throws Exception {
        //given

        //mocking
        when(categoryService.retrieveCategory(category.getId())).thenReturn(categoryResponse);

        //then
        mockMvc.perform(post(URI_PREFIX + "/create")
                .content(objectMapper.writeValueAsString(createDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();
    }

//    @Test
    @DisplayName("카테고리 등록 실패, 입력값 오류")
    void testCreateCategory_ifCreateRequestIncludeNullOrBlank_throwsValidationFailedException()
        throws Exception {
        //given
        CategoryCreateRequest sampleDto = new CategoryCreateRequest();
        //mocking

        //then
        mockMvc.perform(post(URI_PREFIX + "/create")
                .content(objectMapper.writeValueAsString(sampleDto))
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

//    @Test
    @DisplayName("단일 카테고리 검색 실패, 존재하지 않는 카테고리 ID")
    void testRetrieveCategory_ifNotExistedCategoryId() throws Exception {
        //mocking
        when(categoryService.retrieveCategory(category.getId())).thenThrow(
            CategoryNotFoundException.class);

        //then
        mockMvc.perform(get(URI_PREFIX + "/" + category.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("카테고리 리스트 검색 성공")
    void testRetrieveCategoryListWithPageable() throws Exception {
        //given
        Page<CategoryResponse> page = Page.empty();

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
            .andExpect(status().isAccepted())
            .andDo(print())
            .andReturn();

    }

//    @Test
    @DisplayName("카테고리 수정 실패, 입력값 오류")
    void testUpdateCategory_ifUpdateRequestIncludeNullOrBlank_throwsValidationFailedException()
        throws Exception {
        //given
        CategoryUpdateRequest sampleDto = new CategoryUpdateRequest();
        //mocking

        //then
        mockMvc.perform(put(URI_PREFIX + "/" + category.getId())
                .content(objectMapper.writeValueAsString(sampleDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();
    }

    //TODO 삭제에 대한 예외처리 고민
    @Test
    @DisplayName("카테고리 삭제 매핑 테스트")
    void testDeleteCategory() throws Exception {
        //mocking
        doNothing().when(categoryService).deleteCategory(category.getId());

        doThrow(CategoryNotFoundException.class).when(categoryService)
            .deleteCategory(category.getId());

        //then
        mockMvc.perform(delete(URI_PREFIX + "/" + category.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isAccepted())
            .andDo(print())
            .andReturn();

    }
}
