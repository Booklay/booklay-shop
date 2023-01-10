package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.category.CategoryCUDto;
import com.nhnacademy.booklay.server.dto.category.CategoryCreateDto;
import com.nhnacademy.booklay.server.dto.category.CategoryDto;
import com.nhnacademy.booklay.server.dto.category.CategoryUpdateDto;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.category.CategoryNotFoundException;
import com.nhnacademy.booklay.server.exception.category.CreateCategoryFailedException;
import com.nhnacademy.booklay.server.exception.category.UpdateCategoryFailedException;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * javadoc. 카테고리 서비스
 */
@Slf4j
@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void createCategory(CategoryCreateDto createDto) {
        if (!categoryRepository.existsById(createDto.getId())) {
            log.info("Create Category Processing");
            if (taskForCategoryFromDto(createDto)) {
                log.info("Create Category Success : " + createDto.getName());
            } else {
                log.info("Create Category Failed");
                throw new CreateCategoryFailedException(createDto);
            }
        } else {
            log.info("Category ID Already Existed");
        }
    }

    @Transactional(readOnly = true)
    public CategoryResponse retrieveCategory(Long categoryId) {
        CategoryResponse categoryResponse = categoryRepository.findById(categoryId, CategoryResponse.class)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        log.info("Retrieve Category : " + categoryResponse.getName());

        return categoryResponse;
    }

    @Transactional(readOnly = true)
    public Page<CategoryDto> retrieveCategory(Pageable pageable) {
        Page<CategoryDto> page = categoryRepository.findAllBy(pageable, CategoryDto.class);

        log.info("Retrieve Category With Page : " + page.getTotalPages());

        return page;
    }

    public void updateCategory(CategoryUpdateDto updateDto) {
        if (categoryRepository.existsById(updateDto.getId())) {
            log.info("Update Category Processing");
            if (taskForCategoryFromDto(updateDto)) {
                log.info("Update Category Success : " + updateDto.getName());
            } else {
                log.info("Update Category Failed");
                throw new UpdateCategoryFailedException(updateDto);

            }
        } else {
            log.info("Category ID Not Existed");
            throw new CategoryNotFoundException(updateDto.getId());
        }
    }

    public boolean deleteCategory(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            log.info("Delete Category ID : " + categoryId);
            return true;
        } else {
            log.info("Category ID Not Existed");
            return false;
        }

    }

    /**
     * javadoc.
     *
     * @param dto create,update dto.
     * @return .
     */
    private boolean taskForCategoryFromDto(CategoryCUDto dto) {

        try {
            Category parent = categoryRepository.findById(dto.getParentCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.getId()));

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
