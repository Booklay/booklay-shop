package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.category.response.CategoryStepResponse;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    void createCategory(CategoryCreateRequest createRequest);

    CategoryResponse retrieveCategory(Long categoryId);

    PageResponse<CategoryResponse> retrieveCategory(Pageable pageable);

    void updateCategory(CategoryUpdateRequest updateRequest, Long categoryId);

    void deleteCategory(Long categoryId);

    CategoryStepResponse retrieveCategoryStep(Long topCategoryId);
}
