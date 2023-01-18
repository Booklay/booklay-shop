package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;

/**
 * @author 최규태
 */

public interface ProductService {

  Long createBookProduct(CreateUpdateProductBookRequest request) throws Exception;

  Long createSubscribeProduct(CreateUpdateProductSubscribeRequest request) throws Exception;

  Long updateBookProduct(CreateUpdateProductBookRequest request) throws Exception;

  Long updateSubscribeProduct(CreateUpdateProductSubscribeRequest request) throws Exception;

}
