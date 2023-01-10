package com.nhnacademy.booklay.server.service.category;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryServiceImpl categoryService;

    Category category;
    Category parentCategory;
    CategoryCreateRequest createRequest;
    CategoryUpdateRequest updateRequest;

    @BeforeEach
    void Setup() {
        category = Dummy.getDummyCategory();
        parentCategory = category.getParent();

        createRequest = new CategoryCreateRequest();

        ReflectionTestUtils.setField(createRequest, "id", category.getId());
        ReflectionTestUtils.setField(createRequest, "parentCategoryId",
            category.getParent().getId());
        ReflectionTestUtils.setField(createRequest, "name", category.getName());
        ReflectionTestUtils.setField(createRequest, "isExposure", category.getIsExposure());

        updateRequest = new CategoryUpdateRequest();

        ReflectionTestUtils.setField(updateRequest, "id", category.getId());
        ReflectionTestUtils.setField(updateRequest, "parentCategoryId",
            category.getParent().getId());
        ReflectionTestUtils.setField(updateRequest, "name", category.getName());
        ReflectionTestUtils.setField(updateRequest, "isExposure", category.getIsExposure());
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
        assertDoesNotThrow(() -> categoryService.createCategory(createRequest));
    }

    @Test
    @DisplayName("카테고리 생성 실패, 이미 있는 ID를 생성 시도")
    void testCreateCategory_ifAlreadyExistedCategoryId_thenThrowsCategoryAlreadyExistedException() {
        //given

        //mocking
        when(categoryRepository.existsById(createRequest.getId()))
            .thenReturn(true);

        //when

        //then
        assertThatThrownBy(() -> categoryService.createCategory(createRequest))
            .isInstanceOf(CategoryAlreadyExistedException.class)
            .hasMessageContaining(String.valueOf(createRequest.getId()));
    }

    @Test
    @DisplayName("카테고리 생성 실패, 존재하지 않는 부모 카테고리")
    void testCreateCategory_ifNotExistedParentCategoryId_thenThrowsCreateCategoryFailedException() {
        //given

        //mocking
        when(categoryRepository.findById(createRequest.getParentCategoryId()))
            .thenThrow(CategoryNotFoundException.class);

        //when

        //then
        assertThatThrownBy(() -> categoryService.createCategory(createRequest))
            .isInstanceOf(CreateCategoryFailedException.class)
            .hasMessageContaining(String.valueOf(createRequest.getParentCategoryId()));
    }


    @Test
    @DisplayName("카테고리 검색 성공")
    void testRetrieveCategory() {
        CategoryResponse categoryResponse = new CategoryResponse();

        ReflectionTestUtils.setField(categoryResponse, "id", category.getId());
        ReflectionTestUtils.setField(categoryResponse, "parentCategoryId",
            category.getParent().getId());
        ReflectionTestUtils.setField(categoryResponse, "name", category.getName());
        ReflectionTestUtils.setField(categoryResponse, "isExposure", category.getIsExposure());

        //mocking
        when(categoryRepository.findById(category.getId(), CategoryResponse.class)).thenReturn(
            Optional.of(categoryResponse));

        //when
        CategoryResponse actual = categoryService.retrieveCategory(category.getId());

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

        //when

        //then
        assertThatNoException().isThrownBy(() -> categoryService.updateCategory(updateRequest,
            category.getId()));
    }

    @Test
    @DisplayName("카테고리 수정 실패, 존재하지 않는 ID에 대해 수정 시도")
    void testUpdateCategory_ifNotExistedCategoryId_thenThrowsCategoryNotFoundException() {
        //given

        //mocking
        when(categoryRepository.existsById(updateRequest.getId()))
            .thenReturn(false);

        //when

        //then
        assertThatThrownBy(() -> categoryService.updateCategory(updateRequest, category.getId()))
            .isInstanceOf(CategoryNotFoundException.class)
            .hasMessageContaining(String.valueOf(updateRequest.getId()));
    }

    @Test
    @DisplayName("카테고리 수정 실패, 존재하지 않는 부모 카테고리")
    void testUpdateCategory_ifNotExistedParentCategoryId_thenThrowsUpdateCategoryFailedException() {
        //given

        //mocking
        when(categoryRepository.existsById(updateRequest.getId()))
            .thenReturn(true);

        when(categoryRepository.findById(updateRequest.getParentCategoryId()))
            .thenThrow(CategoryNotFoundException.class);

        //when

        //then
        assertThatThrownBy(() -> categoryService.updateCategory(updateRequest, category.getId()))
            .isInstanceOf(UpdateCategoryFailedException.class)
            .hasMessageContaining(String.valueOf(createRequest.getParentCategoryId()));
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