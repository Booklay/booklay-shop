package com.nhnacademy.booklay.server.dto.category.request;

public interface CategoryCURequest {
    Long getId();

    String getName();

    Boolean getIsExposure();

    Long getParentCategoryId();
}
