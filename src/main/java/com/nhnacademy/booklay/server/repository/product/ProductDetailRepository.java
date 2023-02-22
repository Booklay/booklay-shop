package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductDetailRepository
    extends JpaRepository<ProductDetail, Long>, ProductDetailRepositoryCustom {

    boolean existsProductDetailByProductId(Long id);

    ProductDetail findProductDetailByProduct(Product product);

    ProductDetail findProductDetailByProductId(Long id);



    @Modifying
    @Query(nativeQuery = true,
            value = "update product_detail set storage = storage - " +
                    ":productCount where product_no = :productNo and storage >= :productCount")
    int updateProductStock(@Param("productNo") Long productNo, @Param("productCount") Long productCount);

    @Modifying
    @Query(nativeQuery = true,
            value = "update product_detail set storage = storage + " +
                    ":productCount where product_no = :productNo")
    int addProductStock(@Param("productNo") Long productNo, @Param("productCount") Long productCount);
}
