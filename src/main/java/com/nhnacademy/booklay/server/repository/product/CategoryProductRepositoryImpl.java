package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.category.response.ProductBoardCategoryResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.QCategory;
import com.nhnacademy.booklay.server.entity.QCategoryProduct;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CategoryProductRepositoryImpl extends QuerydslRepositorySupport implements
    CategoryProductRepositoryCustom {

  public CategoryProductRepositoryImpl() {
    super(CategoryProduct.class);
  }

  @Override
  public List<ProductBoardCategoryResponse> findCategoryProductsByPkProductId(Product product) {
    QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;
    QCategory category = QCategory.category;

    List<ProductBoardCategoryResponse> result = from(categoryProduct)
        .innerJoin(category)
        .on(category.id.eq(categoryProduct.pk.productId))
        .where(categoryProduct.pk.productId.eq(product.getId()))
        .select(Projections.constructor(ProductBoardCategoryResponse.class,
            )
    return null;
  }
}
