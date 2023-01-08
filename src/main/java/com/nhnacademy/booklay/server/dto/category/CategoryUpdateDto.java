package com.nhnacademy.booklay.server.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class CategoryUpdateDto implements CategoryCUDto {

    @NotNull
    private Long id;

    @NotNull
    private Long parentCategoryId;

    @NotBlank
    @Length(max = 50)
    private String name;

    @NotNull
    private Boolean isExposure;

    public CategoryUpdateDto(Long id, Long parentCategoryId, String name, Boolean isExposure) {
        this.id = id;
        this.parentCategoryId = parentCategoryId;
        this.name = name;
        this.isExposure = isExposure;
    }
}
