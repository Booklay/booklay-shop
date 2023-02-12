package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductRepositoryCustom {

    Page<RetrieveBookForSubscribeResponse> findAllBooksForSubscribeBy(Pageable pageable);

    List<String> findAuthorNameByProductId(Long productId);

    Page<Product> findNotDeletedByPageable(Pageable pageable);

    Page<ProductAllInOneResponse> findProductPage(Pageable pageable);

    Page<ProductAllInOneResponse> findProductPage(List<Long> ids, Pageable pageable);

    List<ProductAllInOneResponse> findProductList(List<Long> ids);

    List<ProductAllInOneResponse> findProductList(List<Long> ids, Pageable pageable);

    ProductAllInOneResponse findProductById(Long id);

    List<ProductAllInOneResponse> findAllProducts();

    List<Product> findAllRecentProduct(Integer recentDay);


}
