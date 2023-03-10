package com.nhnacademy.booklay.server.service.category;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
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
import java.util.List;
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

    private static final String NOT_FOUND_MESSAGE = "???????????? ?????? ???????????? ID??? ?????? ???????????????.";
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
    @DisplayName("???????????? ?????? ??????")
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
    @DisplayName("???????????? ?????? ??????, ?????? ?????? ID??? ?????? ??????")
    void testCreateCategory_ifAlreadyExistedCategoryId_thenThrowsCategoryAlreadyExistedException() {
        //given

        //mocking
        when(categoryRepository.existsById(createRequest.getId()))
            .thenReturn(true);

        //when

        //then
        assertThatThrownBy(() -> categoryService.createCategory(createRequest))
            .isInstanceOf(AlreadyExistedException.class)
            .hasMessageContaining("?????? ???????????? ???????????? ID ?????????.");
    }

    @Test
    @DisplayName("???????????? ?????? ??????, ???????????? ?????? ?????? ????????????")
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
    @DisplayName("???????????? ?????? ??????")
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
    @DisplayName("???????????? ?????? ??????, ???????????? ?????? ID")
    void testRetrieveCategory_ifNotExistedCategoryId_thenThrowsCategoryNotFoundException() {

        //mocking

        //when

        //then
        assertThatThrownBy(() -> categoryService.retrieveCategory(category.getId()))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining(NOT_FOUND_MESSAGE);
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
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
    @DisplayName("???????????? ??????. ???????????? ??????????????? ?????? ?????? ???.")
    void testUpdateCategory_ifNotEqualId() {
        //mocking
        given(categoryRepository.findById(updateRequest.getParentCategoryId())).willReturn(
            Optional.ofNullable(parentCategory));

        given(categoryRepository.findById(category.getId())).willReturn(
            Optional.ofNullable(category));

        ReflectionTestUtils.setField(updateRequest, "id", 2L);

        //when
        categoryService.updateCategory(updateRequest, category.getId());

        //then
        then(categoryRepository).should().delete(any());
    }

    @Test
    @DisplayName("???????????? ?????? ??????, ???????????? ?????? ID??? ?????? ?????? ??????")
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
    @DisplayName("???????????? ?????? ??????, ???????????? ?????? ?????? ????????????")
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
    @DisplayName("???????????? ?????? ??????")
    void testDeleteCategory() {
        when(categoryRepository.existsById(category.getId())).thenReturn(true);

        assertThatNoException().isThrownBy(() -> categoryService.deleteCategory(category.getId()));
    }

    @Test
    @DisplayName("???????????? ?????? ??????, ???????????? ?????? ????????????")
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