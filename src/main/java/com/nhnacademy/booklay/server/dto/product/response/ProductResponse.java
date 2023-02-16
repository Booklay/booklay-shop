package com.nhnacademy.booklay.server.dto.product.response;

import com.nhnacademy.booklay.server.entity.Product;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProductResponse {

    private final Long id;

    private final Long objectFileId;

    private final String title;

    private final LocalDateTime createdAt;

    private final Long price;

    private final Long pointRate;

    private final String shortDescription;

    private final String longDescription;

    private final boolean selling;

    private final boolean pointMethod;

    private final boolean deleted;


    public ProductResponse(Product product) {
        this.id = product.getId();
        this.objectFileId = product.getThumbnailNo();
        this.title = product.getTitle();
        this.createdAt = product.getCreatedAt();
        this.price = product.getPrice();
        this.pointRate = product.getPointRate();
        this.shortDescription = product.getShortDescription();
        this.longDescription = product.getLongDescription();
        this.selling = product.isSelling();
        this.pointMethod = product.isPointMethod();
        this.deleted = product.isDeleted();
    }
}
