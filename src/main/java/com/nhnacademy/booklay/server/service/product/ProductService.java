package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.product.request.CreateProductSubscribeRequest;

public interface ProductService {

  Long createBookProduct(CreateProductBookRequest request) throws Exception;

  Long createSubscribeProduct(CreateProductSubscribeRequest request) throws Exception;

  Long updateBookProduct(CreateProductBookRequest request) throws Exception;

  Long updateSubscribeProduct(CreateProductSubscribeRequest request) throws Exception;

}
