package com.nhnacademy.booklay.server.repository.product.impl;

import com.nhnacademy.booklay.server.entity.ProductRelation;
import com.nhnacademy.booklay.server.entity.QProductRelation;
import com.nhnacademy.booklay.server.repository.product.ProductRelationRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductRelationRepositoryImpl extends QuerydslRepositorySupport implements
    ProductRelationRepositoryCustom {

  public ProductRelationRepositoryImpl() {
    super(ProductRelation.class);
  }

  @Override
  public List<Long> findRecommendIdsByBaseProductId(Long productId) {
    QProductRelation productRelation = QProductRelation.productRelation;

    return from(productRelation)
        .where(productRelation.baseProduct.id.eq(productId))
        .select(productRelation.relatedProduct.id)
        .fetch();
  }

  @Override
  public Boolean existsByBaseAndTargetId(Long baseId, Long targetId) {
    QProductRelation productRelation = QProductRelation.productRelation;

    return from(productRelation)
        .where(productRelation.baseProduct.id.eq(baseId)
            .and(productRelation.relatedProduct.id.eq(targetId)))
        .select(productRelation.id)
        .fetchOne() != null;
  }

  @Override
  public void deleteByBaseAndTargetId(Long baseId, Long targetId) {
    QProductRelation productRelation = QProductRelation.productRelation;

    delete(productRelation)
        .where(productRelation.baseProduct.id.eq(baseId)
            .and(productRelation.relatedProduct.id.eq(targetId)))
        .execute();
  }
}
