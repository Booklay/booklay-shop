package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository
    extends JpaRepository<ProductDetail, Long>, ProductDetailRepositoryCustom {

    boolean existsProductDetailByProductId(Long id);

    ProductDetail findProductDetailByProduct(Product product);

    ProductDetail findProductDetailByProductId(Long id);
}
