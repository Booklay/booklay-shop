package com.nhnacademy.booklay.server.dto.product.category;

import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.entity.Category;
import lombok.Getter;

@Getter
public class CategoriesInProduct {

    Long productId;

    CategoryResponse category;

    public CategoriesInProduct(Long productId, Category category) {
        this.productId = productId;
        this.category = new CategoryResponse(category);
    }
}
