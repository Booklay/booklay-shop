package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductViewResponse;
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

  RetrieveProductBookResponse retrieveBookData(Long id);

  RetrieveProductSubscribeResponse retrieveSubscribeData(Long productId);

  Page<RetrieveProductResponse> retrieveProductPage(Pageable pageable);

  RetrieveProductViewResponse retrieveProductView(Long productId);
}
