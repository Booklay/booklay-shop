package com.nhnacademy.booklay.server.dto.product.tag;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Tag;
import java.util.Objects;
import lombok.Getter;

@Getter
public class TagsInProduct {

    Long productId;

    RetrieveTagResponse tag;

    public TagsInProduct(Long productId, Tag tag) {
        this.productId = productId;
        this.tag = new RetrieveTagResponse(tag);
    }
}
