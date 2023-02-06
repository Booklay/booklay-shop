package com.nhnacademy.booklay.server.dto.product.response;

import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProductAllInOneResponse {

    ProductResponse info;
    ProductDetailResponse detail;
    SubscribeResponse subscribe;

    @Setter
    private List<RetrieveAuthorResponse> authors = new ArrayList<>();

    @Setter
    private List<RetrieveTagResponse> tags = new ArrayList<>();

    @Setter
    private List<CategoryResponse> categories = new ArrayList<>();

    public ProductAllInOneResponse(Product product, ProductDetail detail,
                                   Subscribe subscribe) {
        this.info = new ProductResponse(product);
        this.detail = Objects.nonNull(detail) ? new ProductDetailResponse(detail) : null;
        this.subscribe = Objects.nonNull(subscribe) ? new SubscribeResponse(subscribe) : null;
    }
}
