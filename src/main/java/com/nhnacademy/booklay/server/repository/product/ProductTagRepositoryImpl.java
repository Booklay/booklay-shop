package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.ProductTag;
import com.nhnacademy.booklay.server.entity.QProductTag;
import com.nhnacademy.booklay.server.entity.QTag;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductTagRepositoryImpl extends QuerydslRepositorySupport implements
    ProductTagRepositoryCustom {


  public ProductTagRepositoryImpl() {
    super(ProductTag.class);
  }

  @Override
  public List<RetrieveTagResponse> findTagsByProductId(Long id) {

    QProductTag productTag = QProductTag.productTag;
    QTag tag = QTag.tag;

    List<RetrieveTagResponse> result = from(productTag)
        .innerJoin(tag).on(productTag.tag.id.eq(tag.id))
        .where(productTag.product.id.eq(id))
        .select(Projections.constructor(RetrieveTagResponse.class, tag.id, tag.name)).fetch();

//        .join(tag).on(productTag.tag.id.eq(tag.id))
//        .where(productTag.product.id.eq(id))
//        .select(Projections.constructor(RetrieveTagResponse.class,
//            tag.id,
//            tag.name))
//        .fetchOne();
    return null;
  }
}
