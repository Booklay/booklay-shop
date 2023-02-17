package com.nhnacademy.booklay.server.service.category;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.service.AlreadyExistedException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class CategoryServiceTest {

    private static final String NOT_FOUND_MESSAGE = "존재하지 않는 카테고리 ID에 대한 요청입니다.";
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
            .isInstanceOf(AlreadyExistedException.class)
            .hasMessageContaining("이미 존재하는 카테고리 ID 입니다.");
    }

    @Test
    @DisplayName("카테고리 생성 실패, 존재하지 않는 부모 카테고리")
    void testCreateCategory_ifNotExistedParentCategoryId_thenThrowsCreateCategoryFailedException() {
        //given


        //mocking
        when(categoryRepository.findById(createRequest.getParentCategoryId())).thenThrow(
            new NotFoundException(Category.class, NOT_FOUND_MESSAGE));

        //when

        //then
        assertThatThrownBy(() -> categoryService.createCategory(createRequest))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining(NOT_FOUND_MESSAGE);
    }


    @Test
    @DisplayName("카테고리 검색 성공")
    void testRetrieveCategory() {

        //mocking
        when(categoryRepository.findById(category.getId())).thenReturn(
            Optional.ofNullable(category));

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
        assertThatThrownBy(() -> categoryService.retrieveCategory(category.getId()))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining(NOT_FOUND_MESSAGE);
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    void testUpdateCategory() {
        //mocking
        given(categoryRepository.findById(parentCategory.getId())).willReturn(
            Optional.ofNullable(parentCategory));

        given(categoryRepository.findById(category.getId())).willReturn(
            Optional.ofNullable(category));

        //when

        //then
        assertThatNoException().isThrownBy(() -> categoryService.updateCategory(updateRequest,
            category.getId()));
    }

    @Test
    @DisplayName("카테고리 수정 실패, 존재하지 않는 ID에 대해 수정 시도")
    void testUpdateCategory_ifNotExistedCategoryId_thenThrowsCategoryNotFoundException() {
        //given
        given(categoryRepository.findById(category.getId())).willThrow(new NotFoundException(Category.class, NOT_FOUND_MESSAGE));

        //when

        //then
        assertThatThrownBy(() -> categoryService.updateCategory(updateRequest, category.getId()))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining(NOT_FOUND_MESSAGE);

    }

    @Test
    @DisplayName("카테고리 수정 실패, 존재하지 않는 부모 카테고리")
    void testUpdateCategory_ifNotExistedParentCategoryId_thenThrowsUpdateCategoryFailedException() {

        //given
        when(categoryRepository.findById(updateRequest.getParentCategoryId()))
            .thenThrow(new NotFoundException(Category.class, NOT_FOUND_MESSAGE));

        given(categoryRepository.findById(category.getId())).willReturn(
            Optional.ofNullable(category));

        //when

        //then
        assertThatThrownBy(() -> categoryService.updateCategory(updateRequest, category.getId()))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining(NOT_FOUND_MESSAGE);
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
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining(NOT_FOUND_MESSAGE);

    }

    @Test
    void retrieveCategory(){

        given(categoryRepository.findAllBy(Pageable.unpaged(), CategoryResponse.class)).willReturn(Page.empty());

        categoryService.retrieveCategory(Pageable.unpaged());

        then(categoryRepository).should(times(1)).findAllBy(Pageable.unpaged(), CategoryResponse.class);
    }
}