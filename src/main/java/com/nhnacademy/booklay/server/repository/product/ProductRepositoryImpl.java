package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.QCategory;
import com.nhnacademy.booklay.server.entity.QCategoryProduct;
import com.nhnacademy.booklay.server.entity.QProduct;
import com.nhnacademy.booklay.server.entity.QProductDetail;
import com.nhnacademy.booklay.server.entity.QSubscribe;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductRepositoryImpl extends QuerydslRepositorySupport implements
    ProductRepositoryCustom {

  public ProductRepositoryImpl() {
    super(Product.class);
  }

  @Override
  public RetrieveProductBookResponse findProductBookDataByProductId(Long id) {
    QProduct product = QProduct.product;
    QProductDetail productDetail = QProductDetail.productDetail;

    return from(product)
        .innerJoin(productDetail).on(productDetail.product.id.eq(product.id))
        .where(product.id.eq(id))
        .select(Projections.constructor(RetrieveProductBookResponse.class,
            product.id,
            product.title,
            product.price,
            product.pointRate,
            product.shortDescription,
            product.longDescription,
            product.isSelling,
            product.pointMethod,
            product.registedAt,
            productDetail.id,
            productDetail.isbn,
            productDetail.page,
            productDetail.publisher,
            productDetail.publishedDate,
            productDetail.ebookAddress,
            productDetail.storage
        )).fetchOne();
  }

  @Override
  public RetrieveProductSubscribeResponse findProductSubscribeDataByProductId(Long id) {
    QProduct product = QProduct.product;
    QSubscribe subscribe = QSubscribe.subscribe;
    return null;
  }

  @Override
  public List<Long> findCategoryIdsByProductId(Long id) {
    QProduct product = QProduct.product;
    QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;
    QCategory category = QCategory.category;

    return from(product)
        .innerJoin(categoryProduct).on(categoryProduct.product.id.eq(product.id))
        .innerJoin(category).on(categoryProduct.category.id.eq(category.id))
        .where(product.id.eq(id))
        .select(category.id)
        .fetch();
  }
}
