package com.nhnacademy.booklay.server.service.category;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.category.CategoryCreateDto;
import com.nhnacademy.booklay.server.dto.category.CategoryDto;
import com.nhnacademy.booklay.server.dto.category.CategoryUpdateDto;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Category;
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

    @BeforeEach
    void Setup() {
        category = Dummy.getDummyCategory();
        parentCategory = category.getParent();
    }

    @Test
    @DisplayName("Category Service Create 테스트")
    void testCreateCategory() {
        //given
        CategoryCreateDto createDto = new CategoryCreateDto(
            category.getId(),
            category.getParent().getId(),
            category.getName(),
            category.getIsExposure()
        );

        //mocking
        when(categoryRepository.findById(parentCategory.getId()))
            .thenReturn(Optional.ofNullable(parentCategory));

        //when

        //then
        assertThatNoException().isThrownBy(() -> categoryService.createCategory(createDto));

    }

    @Test
    @DisplayName("Category Service Retrieve 테스트")
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
    @DisplayName("Category Service Update 테스트")
    void testUpdateCategory() {
        CategoryUpdateDto updateDto = new CategoryUpdateDto(
            category.getId(),
            category.getParent().getId(),
            "원서",
            !category.getIsExposure()
        );

        //mocking
        when(categoryRepository.existsById(category.getId())).thenReturn(true);

        when(categoryRepository.findById(parentCategory.getId())).thenReturn(
            Optional.ofNullable(parentCategory));

        //when

        //then
        assertThatNoException().isThrownBy(() -> categoryService.updateCategory(updateDto));

    }

    @Test
    @DisplayName("Category Service Delete 테스트")
    void testDeleteCategory() {
        when(categoryRepository.existsById(category.getId())).thenReturn(true);

        assertThatNoException().isThrownBy(() -> categoryService.deleteCategory(category.getId()));
    }
}