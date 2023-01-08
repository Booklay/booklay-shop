package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.category.CategoryCreateDto;
import com.nhnacademy.booklay.server.dto.category.CategoryDto;
import com.nhnacademy.booklay.server.dto.category.CategoryUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    void createCategory(CategoryCreateDto createDto);

    CategoryDto retrieveCategory(Long id);

    void updateCategory(CategoryUpdateDto updateDto);

    boolean deleteCategory(Long id);

    Page<CategoryDto> retrieveCategory(Pageable pageable);
}
