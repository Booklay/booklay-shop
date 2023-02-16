package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductRepositoryCustom {

    Page<RetrieveBookForSubscribeResponse> findAllBooksForSubscribeBy(Pageable pageable);

    List<String> findAuthorNameByProductId(Long productId);

    Page<Product> findNotDeletedByPageable(Pageable pageable);

    Page<ProductAllInOneResponse> retrieveProductsInPage(Pageable pageable);

    Page<ProductAllInOneResponse> retrieveProductsByIdsInPage(List<Long> ids, Pageable pageable);

    List<ProductAllInOneResponse> retrieveProductsByIds(List<Long> ids);

    List<ProductAllInOneResponse> retrieveProductsByCondition(List<Long> ids, Pageable pageable, Sort sort);

    ProductAllInOneResponse retrieveProductById(Long id);

    List<ProductAllInOneResponse> retrieveAllProducts();

    List<RetrieveAuthorResponse> getAuthorsByProductId(Long productId);

}
