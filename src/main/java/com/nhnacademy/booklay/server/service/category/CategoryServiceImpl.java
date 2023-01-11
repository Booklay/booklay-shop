package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.service.AlreadyExistedException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
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

    public void createCategory(CategoryCreateRequest createRequest) {

        if (categoryRepository.existsById(createRequest.getId())) {
            throw new AlreadyExistedException(Category.class,
                "이미 존재하는 카테고리 ID 입니다.");
        }

        Category parentCategory = categoryRepository.findById(createRequest.getParentCategoryId())
            .orElseThrow(() -> new NotFoundException(Category.class,
                "존재하지 않는 상위 카테고리 ID 입니다."));

        categoryRepository.save(createRequest.toEntity(parentCategory));
    }

    @Transactional(readOnly = true)
    public CategoryResponse retrieveCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NotFoundException(Category.class, "존재하지 않는 카테고리 ID에 대한 요청입니다."));

        return new CategoryResponse().fromEntity(category);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> retrieveCategory(Pageable pageable) {

        return categoryRepository.findAllBy(pageable, CategoryResponse.class);
    }

    public void updateCategory(CategoryUpdateRequest updateDto, Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException(Category.class,
                "존재하지 않는 카테고리 ID 입니다.");
        }

        Category parentCategory = categoryRepository.findById(updateDto.getParentCategoryId())
            .orElseThrow(() -> new NotFoundException(Category.class,
                "존재하지 않는 상위 카테고리 ID 입니다."));

        categoryRepository.save(updateDto.toEntity(parentCategory));

    }

    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException(Category.class, "존재하지 않는 카테고리 ID에 대한 요청입니다.");
        }
        categoryRepository.deleteById(categoryId);
        log.info("Delete Category ID : " + categoryId);

    }

}
