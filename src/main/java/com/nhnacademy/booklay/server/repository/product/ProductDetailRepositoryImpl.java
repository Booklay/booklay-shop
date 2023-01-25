package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductViewResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.QAuthor;
import com.nhnacademy.booklay.server.entity.QProductAuthor;
import com.nhnacademy.booklay.server.entity.QProductDetail;
import com.nhnacademy.booklay.server.entity.QProductTag;
import com.nhnacademy.booklay.server.entity.QTag;
import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductDetailRepositoryImpl extends QuerydslRepositorySupport implements
    ProductDetailRepositoryCustom {

  public ProductDetailRepositoryImpl() {
    super(ProductDetail.class);
  }

  @Override
  public RetrieveProductViewResponse findProductBookByProduct(Product product) {
    QProductDetail productDetail = QProductDetail.productDetail;
    QProductAuthor productAuthor = QProductAuthor.productAuthor;
    QAuthor author = QAuthor.author;
    QProductTag productTag = QProductTag.productTag;
    QTag tag = QTag.tag;

//    from(productDetail)
//        .innerJoin(productAuthor).on(productDetail.id.eq(productAuthor.productDetail.id))
//        .where(productDetail.product.id.eq(product.getId()))
//        .select(Projections.constructor(RetrieveProductViewResponse.class,
//            product,productDetail,
//    from(productAuthor)
//        .innerJoin(author).on(productAuthor.author.authorNo.eq(author.authorNo))
//        .where(productAuthor.productDetail.id.eq(productDetail.id))
//        .select(Projections.constructor(RetrieveAuthorResponse.class,
//            author.authorNo,
//            author.name)))).fetch();
    return null;
  }
}
