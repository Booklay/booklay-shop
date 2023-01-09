package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.CreateProductSubscribeRequest;
import com.nhnacademy.booklay.server.entity.Product;

public interface ProductService {

  Long createBookProduct(CreateProductBookRequest request) throws Exception;

  Long createSubscribeProduct(CreateProductSubscribeRequest request) throws Exception;

  Long updateBookProduct(CreateProductBookRequest request) throws Exception;

  Long updateSubscribeProduct(CreateProductSubscribeRequest request) throws Exception;

}
