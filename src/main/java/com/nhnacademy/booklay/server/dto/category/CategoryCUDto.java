package com.nhnacademy.booklay.server.dto.category;

public interface CategoryCUDto {
    Long getId();

    String getName();

    Boolean getIsExposure();

    Long getParentCategoryId();
}
