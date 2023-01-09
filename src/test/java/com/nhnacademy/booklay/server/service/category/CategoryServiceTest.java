package com.nhnacademy.booklay.server.service.category;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.category.CategoryCreateDto;
import com.nhnacademy.booklay.server.dto.category.CategoryDto;
import com.nhnacademy.booklay.server.dto.category.CategoryUpdateDto;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.category.CategoryAlreadyExistedException;
import com.nhnacademy.booklay.server.exception.category.CategoryNotFoundException;
import com.nhnacademy.booklay.server.exception.category.CreateCategoryFailedException;
import com.nhnacademy.booklay.server.exception.category.UpdateCategoryFailedException;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class CategoryServiceTest {

    @MockBean
    CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;

    Category category;
    Category parentCategory;
    CategoryCreateDto createDto;
    CategoryUpdateDto updateDto;

    @BeforeEach
    void Setup() {
        category = Dummy.getDummyCategory();
        parentCategory = category.getParent();

        createDto = new CategoryCreateDto();

        ReflectionTestUtils.setField(createDto, "id", category.getId());
        ReflectionTestUtils.setField(createDto, "parentCategoryId", category.getParent().getId());
        ReflectionTestUtils.setField(createDto, "name", category.getName());
        ReflectionTestUtils.setField(createDto, "isExposure", category.getIsExposure());

        updateDto = new CategoryUpdateDto();

        ReflectionTestUtils.setField(updateDto, "id", category.getId());
        ReflectionTestUtils.setField(updateDto, "parentCategoryId", category.getParent().getId());
        ReflectionTestUtils.setField(updateDto, "name", category.getName());
        ReflectionTestUtils.setField(updateDto, "isExposure", category.getIsExposure());
    }

    @Test
    @DisplayName("카테고리 생성 성공")
    void testCreateCategory() {
        //given

        //mocking
        when(categoryRepository.findById(parentCategory.getId()))
            .thenReturn(Optional.ofNullable(parentCategory));

        //when


        //then
        assertDoesNotThrow(() -> categoryService.createCategory(createDto));
    }

    @Test
    @DisplayName("카테고리 생성 실패, 이미 있는 ID를 생성 시도")
    void testCreateCategory_ifAlreadyExistedCategoryId_thenThrowsCategoryAlreadyExistedException() {
        //given

        //mocking
        when(categoryRepository.existsById(createDto.getId()))
            .thenReturn(true);

        //when

        //then
        assertThatThrownBy(() -> categoryService.createCategory(createDto))
            .isInstanceOf(CategoryAlreadyExistedException.class)
            .hasMessageContaining(String.valueOf(createDto.getId()));
    }

    @Test
    @DisplayName("카테고리 생성 실패, 존재하지 않는 부모 카테고리")
    void testCreateCategory_ifNotExistedParentCategoryId_thenThrowsCreateCategoryFailedException() {
        //given

        //mocking
        when(categoryRepository.findById(createDto.getParentCategoryId()))
            .thenThrow(CategoryNotFoundException.class);

        //when

        //then
        assertThatThrownBy(() -> categoryService.createCategory(createDto))
            .isInstanceOf(CreateCategoryFailedException.class)
            .hasMessageContaining(String.valueOf(createDto.getParentCategoryId()));
    }


    @Test
    @DisplayName("카테고리 검색 성공")
    void testRetrieveCategory() {
        CategoryDto categoryDto = new CategoryDto(
            category.getId(),
            category.getName()
        );

        //mocking
        when(categoryRepository.findById(category.getId(), CategoryDto.class)).thenReturn(
            Optional.of(categoryDto));

        //when
        CategoryDto actual = categoryService.retrieveCategory(category.getId());

        //then
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(category.getId()),
            () -> assertThat(actual.getName()).isEqualTo(category.getName())
        );
    }

    @Test
    @DisplayName("카테고리 검색 실패, 존재하지 않는 ID")
    void testRetrieveCategory_ifNotExistedCategoryId_thenThrowsCategoryNotFoundException() {

        //mocking

        //when

        //then
        assertThatThrownBy(() -> categoryService.retrieveCategory(category.getId()));
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    void testUpdateCategory() {
        //mocking
        when(categoryRepository.existsById(category.getId())).thenReturn(true);

        when(categoryRepository.findById(parentCategory.getId())).thenReturn(
            Optional.ofNullable(parentCategory));

        when(categoryRepository.findById(category.getId())).thenReturn(
            Optional.ofNullable(category));
        //when

        //then
        assertThatNoException().isThrownBy(() -> categoryService.updateCategory(updateDto));
    }

    @Test
    @DisplayName("카테고리 수정 실패, 존재하지 않는 ID에 대해 수정 시도")
    void testUpdateCategory_ifNotExistedCategoryId_thenThrowsCategoryNotFoundException() {
        //given

        //mocking
        when(categoryRepository.existsById(updateDto.getId()))
            .thenReturn(false);

        //when

        //then
        assertThatThrownBy(() -> categoryService.updateCategory(updateDto))
            .isInstanceOf(CategoryNotFoundException.class)
            .hasMessageContaining(String.valueOf(updateDto.getId()));
    }

    @Test
    @DisplayName("카테고리 수정 실패, 존재하지 않는 부모 카테고리")
    void testUpdateCategory_ifNotExistedParentCategoryId_thenThrowsUpdateCategoryFailedException() {
        //given

        //mocking
        when(categoryRepository.existsById(updateDto.getId()))
            .thenReturn(true);

        when(categoryRepository.findById(updateDto.getParentCategoryId()))
            .thenThrow(CategoryNotFoundException.class);

        //when

        //then
        assertThatThrownBy(() -> categoryService.updateCategory(updateDto))
            .isInstanceOf(UpdateCategoryFailedException.class)
            .hasMessageContaining(String.valueOf(createDto.getParentCategoryId()));
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void testDeleteCategory() {
        when(categoryRepository.existsById(category.getId())).thenReturn(true);

        assertThatNoException().isThrownBy(() -> categoryService.deleteCategory(category.getId()));
    }

    @Test
    @DisplayName("카테고리 삭제 실패, 존재하지 않는 카테고리")
    void testDeleteCategory_ifNotExistedCategoryId_thenThrowsCategoryNotFoundException() {
        when(categoryRepository.existsById(category.getId())).thenReturn(false);

        assertThatThrownBy(() -> categoryService.deleteCategory(category.getId()))
            .isInstanceOf(CategoryNotFoundException.class)
            .hasMessageContaining(String.valueOf(category.getId()));
    }
}