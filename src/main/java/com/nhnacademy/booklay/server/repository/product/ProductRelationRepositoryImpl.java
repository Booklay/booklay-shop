package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.ProductRelation;
import com.nhnacademy.booklay.server.entity.QProductRelation;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductRelationRepositoryImpl extends QuerydslRepositorySupport implements ProductRelationRepositoryCustom {

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
}
