package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        entityManager.persist(Dummy.getDummyCategory().getParent());
    }

    @Test
    @DisplayName("CategoryRepository save 테스트")
    void testCategorySave() {
        //given
        Category category = Dummy.getDummyCategory();

        //when
        Category expected = categoryRepository.save(category);

        //then
        assertThat(expected.getName()).isEqualTo(category.getName());
    }

    @Test
    @DisplayName("CategoryRepository findById 테스트")
    void testCategoryFindById() {
        //given
        Category category = Dummy.getDummyCategory();
        categoryRepository.save(category);

        //when
        Category expected = categoryRepository.findById(category.getId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));


        //then
        assertThat(expected.getId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("CategoryRepository update 테스트")
    void testCategoryUpdate() {
        //given
        Category category = Dummy.getDummyCategory();
        categoryRepository.save(category);

        Category expected = Category.builder()
            .id(category.getId())
            .parent(category.getParent())
            .name("외국도서")
            .depth(category.getDepth())
            .isExposure(category.getIsExposure())
            .build();

        categoryRepository.save(expected);

        //when
        Category actual = categoryRepository.findById(category.getId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        //then
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(category.getId()),
            () -> assertThat(actual.getName()).isNotEqualTo(category.getName()),

            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    @DisplayName("CategoryRepository delete 테스트")
    void testCategoryDelete() {
        //given
        Category category = Dummy.getDummyCategory();
        categoryRepository.save(category);

        //when
        categoryRepository.deleteById(category.getId());

        //then
        assertThatThrownBy(() ->
            categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"))
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
