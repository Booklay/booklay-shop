package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository extends JpaRepository<ProductTag, ProductTag.Pk> {

  void deleteByPk_TagId(Long id);

  boolean existsByPk_TagId(Long id);
}
