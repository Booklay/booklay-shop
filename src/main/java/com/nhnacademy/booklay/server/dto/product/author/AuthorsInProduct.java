package com.nhnacademy.booklay.server.dto.product.author;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import lombok.Getter;

@Getter
public class AuthorsInProduct {
    Long productId;

    RetrieveAuthorResponse author;

    public AuthorsInProduct(Long productId, RetrieveAuthorResponse author) {
        this.productId = productId;
        this.author = author;
    }
}
