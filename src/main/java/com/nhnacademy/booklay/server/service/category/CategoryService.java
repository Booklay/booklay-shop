package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.category.CategoryCreateDto;
import com.nhnacademy.booklay.server.dto.category.CategoryCUDto;
import com.nhnacademy.booklay.server.dto.category.CategoryDto;
import com.nhnacademy.booklay.server.dto.category.CategoryUpdateDto;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.category.CreateCategoryFailedException;
import com.nhnacademy.booklay.server.exception.category.CategoryNotFoundException;
import com.nhnacademy.booklay.server.exception.category.UpdateCategoryFailedException;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * javadoc. 카테고리 서비스
 */
@Slf4j
@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = false)
    public void createCategory(CategoryCreateDto createDto) {
        if (!categoryRepository.existsById(createDto.getId())) {
            log.info("Create Category Processing");
            if (!taskForCategoryFromDto(createDto)) {
                log.info("Create Category Failed");
                throw new CreateCategoryFailedException();
            }
        } else {
            log.info("Category ID Already Existed");
        }
    }

    public CategoryDto retrieveCategory(Long categoryId) {
        return categoryRepository.findById(categoryId, CategoryDto.class)
            .orElseThrow(CategoryNotFoundException::new);
    }

    @Transactional(readOnly = false)
    public void updateCategory(CategoryUpdateDto updateDto) {
        if (categoryRepository.existsById(updateDto.getId())) {
            log.info("Update Category Processing");
            if (!taskForCategoryFromDto(updateDto)) {
                log.info("Update Category Failed");
                throw new UpdateCategoryFailedException();
            }
        } else {
            log.info("Category ID Not Existed");
            throw new CategoryNotFoundException();
        }
    }

    @Transactional(readOnly = false)
    public void deleteCategory(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
        } else {
            log.info("Category ID Not Existed");
            throw new CategoryNotFoundException();
        }
    }

    private boolean taskForCategoryFromDto(CategoryCUDto dto) {

        try {
            Category parent = categoryRepository.findById(dto.getParentCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

            Category category = Category.builder()
                .id(dto.getId())
                .parent(parent)
                .name(dto.getName())
                .depth(parent.getDepth() + 1L)
                .isExposure(dto.getIsExposure())
                .build();

            categoryRepository.save(category);

            return true;
        } catch (Exception e) {
            log.info(
                "Exception Occurred : " + e.getMessage()
                    + "\nException Caused By " + e.getCause()
            );
            return false;
        }

    }

}
