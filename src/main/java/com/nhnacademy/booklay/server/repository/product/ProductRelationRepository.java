package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.ProductRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRelationRepository extends JpaRepository<ProductRelation, ProductRelation>,
    ProductRelationRepositoryCustom {

  Boolean existsAllByBaseProduct_Id(Long id);
}
