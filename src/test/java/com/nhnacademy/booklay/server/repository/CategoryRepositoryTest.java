package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void testCategorySave() {
        //given
        Category category = Dummy.getDummyCategory();
        entityManager.persist(category.getParent());

        //when
        Category expected = categoryRepository.save(category);

        //then
        assertThat(expected.getName()).isEqualTo(category.getName());
    }
}