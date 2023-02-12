package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.entity.Product;
import java.io.IOException;
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

  ProductAllInOneResponse retrieveBookData(Long id);

  Page<RetrieveProductResponse> retrieveProductPage(Pageable pageable) throws IOException;


  List<Product> retrieveProductListByProductNoList(List<Long> productNoList);


  Page<RetrieveBookForSubscribeResponse> retrieveBookDataForSubscribe(Pageable pageable,
      Long subscribeId);

  void softDelete(Long productId);


  List<RetrieveProductResponse> retrieveProductResponses(List<Long> productIds)
      throws IOException;

  Page<RetrieveProductResponse> retrieveAdminProductPage(Pageable pageable) throws IOException;

  ProductAllInOneResponse findProductById(Long productId);

  Page<ProductAllInOneResponse> getProductsPage(Pageable pageable);

}
