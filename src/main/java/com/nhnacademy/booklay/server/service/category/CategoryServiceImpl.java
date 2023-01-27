package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.category.response.CategoryStepResponse;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.service.AlreadyExistedException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
import java.util.Objects;
import java.util.Optional;
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

    private static final String NOT_FOUND_MESSAGE = "존재하지 않는 카테고리 ID에 대한 요청입니다.";

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(CategoryCreateRequest createRequest) {

        if (categoryRepository.existsById(createRequest.getId())) {
            throw new AlreadyExistedException(Category.class,
                "이미 존재하는 카테고리 ID 입니다.");
        }

        Optional<Category> parentCategory = Optional.empty();

        if (Objects.nonNull(createRequest.getParentCategoryId())) {
            if (createRequest.getParentCategoryId() != 0) {
                parentCategory =
                    categoryRepository.findById(createRequest.getParentCategoryId());
            }
        }

        categoryRepository.save(createRequest.toEntity(parentCategory));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse retrieveCategory(Long categoryId) {
        Category category = categoryFindById(categoryId);

        return new CategoryResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> retrieveCategory(Pageable pageable) {

        Page<CategoryResponse> page =
            categoryRepository.findAllBy(pageable, CategoryResponse.class);

        return new PageResponse<>(page);
    }

    @Override
    public void updateCategory(CategoryUpdateRequest updateRequest, Long categoryId) {
        Category category = categoryFindById(categoryId);

        Optional<Category> parentCategory = Optional.empty();

        if (updateRequest.getParentCategoryId() != 0) {
            parentCategory =
                categoryRepository.findById(updateRequest.getParentCategoryId());
        }

        categoryRepository.delete(category);
        categoryRepository.save(updateRequest.toEntity(parentCategory));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException(Category.class, NOT_FOUND_MESSAGE);
        }
        categoryRepository.deleteById(categoryId);
        log.info("Delete Category ID : " + categoryId);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryStepResponse retrieveCategoryStep(Long topCategoryId) {

        Category category = categoryFindById(topCategoryId);

        return CategoryStepResponse.builder()
            .category(category)
            .build();
    }

    private Category categoryFindById(Long categoryId) {

        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NotFoundException(Category.class, NOT_FOUND_MESSAGE));
    }

}
