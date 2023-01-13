package com.nhnacademy.booklay.server.dto.category.response;

import com.nhnacademy.booklay.server.entity.Category;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private Long parentCategoryId;
    private Long depth;
    private Boolean isExposure;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        if (Objects.nonNull(category.getParent())) {
            this.parentCategoryId = category.getParent().getId();
        }
        this.depth = category.getDepth();
        this.isExposure = category.getIsExposure();
    }
}
