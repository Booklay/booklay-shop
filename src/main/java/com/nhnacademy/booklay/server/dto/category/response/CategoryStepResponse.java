package com.nhnacademy.booklay.server.dto.category.response;

import com.nhnacademy.booklay.server.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryStepResponse {

    Long id;
    Long parentId;

    String name;

    List<CategoryStepResponse> categories = new ArrayList<>();

//    @Builder
    public CategoryStepResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parentId = category.getParentCategoryNo();
    }
}
