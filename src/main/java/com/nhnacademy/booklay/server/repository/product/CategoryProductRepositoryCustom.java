package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.category.response.ProductBoardCategoryResponse;
import com.nhnacademy.booklay.server.entity.Product;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CategoryProductRepositoryCustom {
  List<ProductBoardCategoryResponse> findCategoryProductsByPkProductId(Product product);
}
