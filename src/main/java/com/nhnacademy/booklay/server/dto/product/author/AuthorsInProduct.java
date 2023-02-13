package com.nhnacademy.booklay.server.dto.product.author;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.entity.Author;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;

@Getter
public class AuthorsInProduct {

    Long productId;

    RetrieveAuthorResponse author;

    public AuthorsInProduct(Long productId, Author author) {
        this.productId = productId;
        this.author = new RetrieveAuthorResponse(author);
    }

}
