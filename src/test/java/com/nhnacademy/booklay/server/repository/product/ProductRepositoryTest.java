package com.nhnacademy.booklay.server.repository.product;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductRepository productRepository;

    @Disabled
    @Test
    @DisplayName("AuthorityRepository save test")
    void testProductSave() {
        //given
        CreateUpdateProductBookRequest request = DummyCart.getDummyProductBookDto();
        Product product = DummyCart.getDummyProduct(request);

        //when
        entityManager.persist(product.getObjectFile());
        Product expected = productRepository.save(product);

        //then
        assertThat(expected.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("AuthorityRepository save test")
    void findAllBooksForSubscribeBy() {
        //given
        PageRequest request = PageRequest.of(1, 1);

        //when
        productRepository.findAllBooksForSubscribeBy(request);

        //then
    }

    @Test
    @DisplayName("AuthorityRepository save test")
    void findAuthorNameByProductId() {
        //given
        Long targetId = 1L;

        //when
        productRepository.findAuthorNameByProductId(targetId);

        //then
    }

    @Test
    @DisplayName("AuthorityRepository save test")
    void findNotDeletedByPageable() {
        //given
        PageRequest request = PageRequest.of(1, 1);

        //when
        productRepository.findNotDeletedByPageable(request);

        //then
    }

    @Test
    @DisplayName("AuthorityRepository save test")
    void retrieveProductsByCondition() {
        //given
        PageRequest request = PageRequest.of(1, 1);
        Sort sort = Sort.by(Sort.Direction.DESC, "vDate");
        //when
        productRepository.retrieveProductsByCondition(List.of(1L, 2L), request, sort);

        //then
    }

    @Test
    @DisplayName("AuthorityRepository save test")
    void retrieveProductsByIdsInPage() {
        //given
        PageRequest request = PageRequest.of(1, 1);
        //when
        productRepository.retrieveProductsByIdsInPage(List.of(1L, 2L), request);

        //then
    }

    @Test
    @DisplayName("AuthorityRepository save test")
    void getAuthorsByProductId() {
        //given
        Long targetId = 1L;
        //when
        productRepository.getAuthorsByProductId(targetId);

        //then
    }
}
