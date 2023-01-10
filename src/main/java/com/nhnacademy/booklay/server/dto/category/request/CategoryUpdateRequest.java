package com.nhnacademy.booklay.server.dto.category.request;

import com.nhnacademy.booklay.server.entity.Category;
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

    @NotNull
    private Long parentCategoryId;

    @NotBlank
    @Length(max = 50)
    private String name;

    @NotNull
    private Boolean isExposure;

    public Category toEntity(Category parentCategory) {
        return Category.builder()
            .id(id)
            .parent(parentCategory)
            .name(name)
            .depth(parentCategory.getDepth() + 1L)
            .isExposure(isExposure)
            .build();
    }
}
