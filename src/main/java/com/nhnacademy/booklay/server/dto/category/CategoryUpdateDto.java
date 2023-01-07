package com.nhnacademy.booklay.server.dto.category;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@RequiredArgsConstructor
public class CategoryUpdateDto implements CategoryCUDto {

    @NotBlank
    private final Long id;

    @NotBlank
    private final Long parentCategoryId;

    @NotBlank
    @Length(max = 50)
    private final String name;

    @NotBlank
    private final Boolean isExposure;

}
