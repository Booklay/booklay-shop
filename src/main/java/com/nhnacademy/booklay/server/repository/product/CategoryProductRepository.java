package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryProductRepository extends
    JpaRepository<CategoryProduct, CategoryProduct.Pk> {

  @Modifying
  @Transactional
  @Query(value = "delete from CategoryProduct as cp where cp.pk.productId=?1")
  void deleteAllByProductId(Long id);
}
