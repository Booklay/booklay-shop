package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.request.DisAndConnectBookWithSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.entity.BookSubscribe;
import com.nhnacademy.booklay.server.entity.BookSubscribe.Pk;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.product.BookSubscribeRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.product.BookSubscribeService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookSubscribeServiceImpl implements BookSubscribeService {

    private final BookSubscribeRepository bookSubscribeRepository;
    private final SubscribeRepository subscribeRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public void bookSubscribeConnection(DisAndConnectBookWithSubscribeRequest request) {
        Subscribe subscribe = subscribeRepository.findById(request.getSubscribeId())
            .orElseThrow(() -> new NotFoundException(Subscribe.class, "subscribe not found"));
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new NotFoundException(Product.class, "product not found"));

        BookSubscribe.Pk pk = new Pk(request.getSubscribeId(), request.getProductId());
        BookSubscribe bookSubscribe = BookSubscribe.builder()
                                                   .pk(pk)
                                                   .releaseDate(request.getReleaseDate())
                                                   .subscribeNo(subscribe)
                                                   .productNo(product)
                                                   .build();
        bookSubscribeRepository.save(bookSubscribe);
    }

    @Override
    public void bookSubscribeDisconnection(DisAndConnectBookWithSubscribeRequest request) {
        BookSubscribe.Pk pk = new Pk(request.getSubscribeId(), request.getProductId());
        if (bookSubscribeRepository.existsById(pk)) {
            bookSubscribeRepository.deleteById(pk);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RetrieveProductResponse> retrieveBookSubscribe(Long subscribeId) throws IOException {
        List<Long> productIds =
            bookSubscribeRepository.findBooksProductIdBySubscribeId(subscribeId);

        return productService.retrieveProductResponses(productIds);
    }
}
