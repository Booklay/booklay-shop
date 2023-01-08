package com.nhnacademy.booklay.server.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@RequiredArgsConstructor
public class CategoryCreateDto implements CategoryCUDto {

    @NotNull
    private final Long id;

    @NotNull
    private final Long parentCategoryId;

    @NotBlank
    @Length(max = 50)
    private final String name;

    @NotNull
    private final Boolean isExposure;

}
