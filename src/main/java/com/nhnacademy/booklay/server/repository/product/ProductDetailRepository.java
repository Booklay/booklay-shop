package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

  @Modifying
  @Query("update ProductDetail as pd "
      + "set pd.isbn = :#{#productDetail.isbn}, pd.page = :#{#productDetail.page}, "
      + "pd.publishedDate = :#{#productDetail.publishedDate}, pd.publisher = :#{#productDetail.publisher}, "
      + "pd.ebookAddress = :#{#productDetail.ebookAddress}, pd.storage= :#{#productDetail.storage} where pd.id = ?1")
  void updateProductDetailById(Long id, ProductDetail productDetail);
}
