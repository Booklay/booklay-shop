package com.nhnacademy.booklay.server.dto.search.response;


import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.entity.document.AuthorInfo;
import com.nhnacademy.booklay.server.entity.document.ProductDocument;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class SearchProductResponse {
    private final Long id;
    private final Long thumbnail;
    private final String title;
    private final String publisher;
    private final Long price;
    private final Boolean selling;
    private final List<AuthorInfo> authors;

    public SearchProductResponse(ProductDocument document) {
        this.id = document.getId();
        this.thumbnail = document.getThumbnail();
        this.title = document.getTitle();
        this.publisher = document.getPublisher();
        this.price = document.getPrice();
        this.selling = document.getIsSelling();
        this.authors = document.getAuthors();
    }

    public SearchProductResponse(RetrieveProductResponse retrieveProductResponse) {
        this.id = retrieveProductResponse.getProductId();
        this.thumbnail = retrieveProductResponse.getObjectFileId();
        this.title = retrieveProductResponse.getTitle();
        this.publisher = retrieveProductResponse.getPublisher();
        this.price = retrieveProductResponse.getPrice();
        this.selling = retrieveProductResponse.getSelling();
        if (retrieveProductResponse.getAuthors()==null){
            this.authors = null;
            return;
        }
        this.authors = retrieveProductResponse.getAuthors().stream()
                .map(author -> new AuthorInfo(author.getAuthorNo(), author.getName()))
                .collect(Collectors.toList());
    }
}
