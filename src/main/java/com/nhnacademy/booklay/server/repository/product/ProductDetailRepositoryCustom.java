package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductViewResponse;
import com.nhnacademy.booklay.server.entity.Product;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductDetailRepositoryCustom {
  RetrieveProductViewResponse findProductBookByProduct(Product product);
}
