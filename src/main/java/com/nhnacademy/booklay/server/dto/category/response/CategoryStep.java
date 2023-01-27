package com.nhnacademy.booklay.server.dto.category.response;

import java.util.List;

public interface CategoryStep {
    String getId();

    String getName();

    List<CategoryStep> getCategories();
}
