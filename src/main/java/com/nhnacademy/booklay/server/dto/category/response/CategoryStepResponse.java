package com.nhnacademy.booklay.server.dto.category.response;

import com.nhnacademy.booklay.server.entity.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryStepResponse {

    Long id;

    String name;

    List<CategoryStepResponse> categories = new ArrayList<>();

    @Builder
    public CategoryStepResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();

        for (Category c : category.getCategories()) {
            categories.add(
                CategoryStepResponse.builder()
                    .category(c)
                    .build()
            );
        }
    }
}
