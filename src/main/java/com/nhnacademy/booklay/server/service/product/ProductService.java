package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.RetrieveIdRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductViewResponse;
import com.nhnacademy.booklay.server.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 최규태
 */

public interface ProductService {

  Long createBookProduct(CreateUpdateProductBookRequest request) throws Exception;

  Long createSubscribeProduct(CreateUpdateProductSubscribeRequest request) throws Exception;

  Long updateBookProduct(CreateUpdateProductBookRequest request) throws Exception;

  Long updateSubscribeProduct(CreateUpdateProductSubscribeRequest request) throws Exception;

  Page<RetrieveProductResponse> retrieveProductPage(Pageable pageable);

  RetrieveProductViewResponse retrieveProductView(RetrieveIdRequest request);

  Product retrieveProductByProductNo(Long productNo);
  List<Product> retrieveProductListByProductNoList(List<Long> productNoList);
}
