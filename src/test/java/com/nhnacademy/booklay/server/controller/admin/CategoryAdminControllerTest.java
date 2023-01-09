package com.nhnacademy.booklay.server.controller.admin;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.category.CategoryCreateDto;
import com.nhnacademy.booklay.server.dto.category.CategoryDto;
import com.nhnacademy.booklay.server.dto.category.CategoryUpdateDto;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Category;
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
    CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        category = Dummy.getDummyCategory();
        categoryDto = new CategoryDto(
            category.getId(),
            category.getName()
        );
    }

    @Test
    @DisplayName("Controller 연결 확인용 테스트")
    void testMapping() throws Exception {
        //then
        mockMvc.perform(get("/admin/category/test"))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @DisplayName("카테고리 등록 매핑 테스트")
    void testRegisterCategory() throws Exception {
        //given
        CategoryCreateDto createDto = new CategoryCreateDto();

        ReflectionTestUtils.setField(createDto, "id", category.getId());
        ReflectionTestUtils.setField(createDto, "parentCategoryId", category.getParent().getId());
        ReflectionTestUtils.setField(createDto, "name", category.getName());
        ReflectionTestUtils.setField(createDto, "isExposure", category.getIsExposure());

        //mocking
        when(categoryService.retrieveCategory(category.getId())).thenReturn(categoryDto);

        //then
        mockMvc.perform(post("/admin/category/register")
                .content(objectMapper.writeValueAsString(createDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();

    }

    @Test
    @DisplayName("카테고리 리스트 검색 매핑 테스트")
    void testGetCategoryList() throws Exception {
        //given
        Page<CategoryDto> page = Page.empty();

        //mocking
        when(categoryService.retrieveCategory(Pageable.unpaged())).thenReturn(page);

        //then
        mockMvc.perform(get("/admin/category/all"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("단일 카테고리 검색 매핑 테스트")
    void testGetCategory() throws Exception {
        //mocking
        when(categoryService.retrieveCategory(category.getId())).thenReturn(categoryDto);

        //then
        mockMvc.perform(get("/admin/category")
                .param("categoryId", String.valueOf(category.getId()))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    }

    @Test
    @DisplayName("카테고리 수정 매핑 테스트")
    void testModifyCategory() throws Exception {
        //given
        CategoryUpdateDto updateDto = new CategoryUpdateDto();

        ReflectionTestUtils.setField(updateDto, "id", category.getId());
        ReflectionTestUtils.setField(updateDto, "parentCategoryId", category.getParent().getId());
        ReflectionTestUtils.setField(updateDto, "name", category.getName());
        ReflectionTestUtils.setField(updateDto, "isExposure", category.getIsExposure());

        //mocking
        when(categoryService.retrieveCategory(category.getId())).thenReturn(categoryDto);

        //then
        mockMvc.perform(put("/admin/category/update")
                .content(objectMapper.writeValueAsString(updateDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isAccepted())
            .andDo(print())
            .andReturn();

    }

    @Test
    @DisplayName("카테고리 삭제 매핑 테스트")
    void testDeleteCategory() throws Exception {
        //mocking

        //then
        mockMvc.perform(get("/admin/category/delete")
                .param("categoryId", String.valueOf(category.getId()))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isAccepted())
            .andDo(print())
            .andReturn();

    }
}
