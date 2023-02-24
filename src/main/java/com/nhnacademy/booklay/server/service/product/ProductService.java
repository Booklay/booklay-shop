package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.search.request.SearchIdRequest;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchProductResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.exception.service.NotEnoughStockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

/**
 * @author 최규태
 */

public interface ProductService {

  Long createBookProduct(CreateUpdateProductBookRequest request) throws IOException;

  Long createSubscribeProduct(CreateUpdateProductSubscribeRequest request) throws IOException;

  Long updateBookProduct(CreateUpdateProductBookRequest request) throws IOException;

  Long updateSubscribeProduct(CreateUpdateProductSubscribeRequest request) throws IOException;

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

//    @Transactional(isolation = Isolation.SERIALIZABLE)
    Boolean storageSoldOutChecker(List<CartDto> cartDtoList) throws NotEnoughStockException;

//    @Transactional(isolation = Isolation.SERIALIZABLE)
    Boolean storageRefund(List<CartDto> cartDtoList) throws NotEnoughStockException;

    SearchPageResponse<SearchProductResponse> getAllProducts(Pageable pageable);

    List<Long> retrieveLatestEightsIds();

    List<SearchProductResponse> getLatestEights();

    SearchPageResponse<SearchProductResponse> retrieveProductByRequest(SearchIdRequest request,
                                                                       Pageable pageable);
}
