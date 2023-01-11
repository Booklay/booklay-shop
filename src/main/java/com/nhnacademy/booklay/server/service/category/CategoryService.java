package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    void createCategory(CategoryCreateRequest createRequest);

    CategoryResponse retrieveCategory(Long categoryId);

    Page<CategoryResponse> retrieveCategory(Pageable pageable);

    void updateCategory(CategoryUpdateRequest updateRequest, Long categoryId);

    void deleteCategory(Long categoryId);
}
