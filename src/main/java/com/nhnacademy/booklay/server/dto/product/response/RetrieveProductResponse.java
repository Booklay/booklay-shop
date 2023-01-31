package com.nhnacademy.booklay.server.dto.product.response;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RetrieveProductResponse {

    @NotNull
    private Long productId;
    @Setter
    private Long objectFileId;
    @NotNull
    private String title;
    @NotNull
    private Long price;
    @NotNull
    private Long pointRate;
    @NotNull
    private Boolean isSelling;
    @NotNull
    private Boolean pointMethod;
    @NotNull
    private String shortDescription;
    @NotNull
    private String publisher;

    @Setter
    private List<RetrieveAuthorResponse> authors;

    public RetrieveProductResponse(Product product, ProductDetail productDetail,
                                   List<RetrieveAuthorResponse> authorsNo) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.pointRate = product.getPointRate();
        this.isSelling = product.isSelling();
        this.pointMethod = product.isPointMethod();
        this.shortDescription = product.getShortDescription();
        this.publisher = productDetail.getPublisher();
        this.authors = authorsNo;
    }

    public RetrieveProductResponse(Product product, Subscribe subscribe) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.pointRate = product.getPointRate();
        this.isSelling = product.isSelling();
        this.pointMethod = product.isPointMethod();
        this.shortDescription = product.getShortDescription();
        this.publisher = subscribe.getPublisher();

    }

    public RetrieveProductResponse(Product product, ProductDetail productDetail, Subscribe subscribe) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.pointRate = product.getPointRate();
        this.isSelling = product.isSelling();
        this.pointMethod = product.isPointMethod();
        this.shortDescription = product.getShortDescription();
        this.objectFileId = product.getObjectFile().getId();

        if (Objects.nonNull(subscribe)) {
            this.publisher = subscribe.getPublisher();
        } else if (Objects.nonNull(productDetail)) {
            this.publisher = productDetail.getPublisher();
        }
    }
}
