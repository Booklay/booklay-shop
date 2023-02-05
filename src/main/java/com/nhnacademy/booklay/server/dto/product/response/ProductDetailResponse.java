package com.nhnacademy.booklay.server.dto.product.response;

import com.nhnacademy.booklay.server.entity.ProductDetail;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ProductDetailResponse {

     private final Long id;
     private final String isbn;
     private final Integer page;
     private final String publisher;
     private final LocalDate publishedDate;
     private final String ebookAddress;
     private final Integer storage;

    public ProductDetailResponse(ProductDetail detail) {
        this.id = detail.getId();
        this.isbn = detail.getIsbn();
        this.page = detail.getPage();
        this.publisher = detail.getPublisher();
        this.publishedDate = detail.getPublishedDate();
        this.ebookAddress = detail.getEbookAddress();
        this.storage = detail.getStorage();
    }
}
