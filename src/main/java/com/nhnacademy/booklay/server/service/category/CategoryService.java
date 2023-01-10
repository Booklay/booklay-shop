package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    void createCategory(CategoryCreateRequest createRequest);

    CategoryResponse retrieveCategory(Long id);

    void updateCategory(CategoryUpdateRequest updateDto, Long categoryId);

    void deleteCategory(Long id);

    Page<CategoryResponse> retrieveCategory(Pageable pageable);
}