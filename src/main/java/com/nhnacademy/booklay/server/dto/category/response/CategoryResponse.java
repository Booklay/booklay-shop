package com.nhnacademy.booklay.server.dto.category.response;

import com.nhnacademy.booklay.server.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private Long parentCategoryId;
    private Long depth;
    private Boolean isExposure;

    public CategoryResponse fromEntity(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parentCategoryId = category.getParent().getId();
        this.depth = category.getDepth();
        this.isExposure = category.getIsExposure();

        return this;
    }
}
