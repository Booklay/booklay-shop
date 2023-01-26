package com.nhnacademy.booklay.server.dto.category.request;

import com.nhnacademy.booklay.server.entity.Category;
import java.util.Optional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequest {

    @NotNull
    private Long id;

    private Long parentCategoryId;

    @NotBlank
    @Length(max = 50)
    private String name;

    @NotNull
    private Boolean isExposure;

    public Category toEntity(Optional<Category> parentCategory) {

        Long depth;
        Category category;

        if (parentCategory.isPresent()) {
            category = parentCategory.get();
            depth = parentCategory.get().getDepth();
        } else {
            category = null;
            depth = 0L;
        }

        return Category.builder()
            .id(id)
            .parent(category)
            .name(name)
            .depth(++depth)
            .isExposure(isExposure)
            .build();
    }
}
