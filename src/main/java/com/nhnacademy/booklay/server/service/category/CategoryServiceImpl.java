package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCURequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.category.CategoryAlreadyExistedException;
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

    /**
     * javadoc.
     * createCategory 와 updateCategory 메소드의 공통 부분을 추출하였고
     * 전체적인 예외처리 과정은 비슷하지만 리팩토링 과정이 오히려 복잡할 것 같아 추가적인 추출은 하지 않음
     *
     * @param createDto ..
     */
    public void createCategory(CategoryCreateRequest createDto) {
        if (!categoryRepository.existsById(createDto.getId())) {
            log.info("Create Category Processing");
            try {
                taskForCategoryFromDto(createDto);
                log.info("Create Category Success : " + createDto.getName());
            } catch (CategoryNotFoundException e) {
                log.info("Create Category Failed");
                throw new CreateCategoryFailedException(createDto);
            }
        } else {
            log.info("Category ID Already Existed");
            throw new CategoryAlreadyExistedException(createDto.getId());
        }
    }

    @Transactional(readOnly = true)
    public CategoryResponse retrieveCategory(Long categoryId) {
        CategoryResponse
            categoryResponse = categoryRepository.findById(categoryId, CategoryResponse.class)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        log.info("Retrieve Category : " + categoryResponse.getName());

        return categoryResponse;
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> retrieveCategory(Pageable pageable) {
        Page<CategoryResponse> page =
            categoryRepository.findAllBy(pageable, CategoryResponse.class);

        log.info("Retrieve Category With Page : " + page.getTotalPages());

        return page;
    }

    public void updateCategory(CategoryUpdateRequest updateDto, Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            log.info("Update Category Processing");
            try {
                deleteCategory(categoryId);
                taskForCategoryFromDto(updateDto);
                log.info("Update Category Success : " + updateDto.getName());
            } catch (CategoryNotFoundException e) {
                log.info("Update Category Failed");
                throw new UpdateCategoryFailedException(updateDto);
            }
        } else {
            log.info("Category ID Not Existed");
            throw new CategoryNotFoundException(categoryId);
        }
    }

    public void deleteCategory(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            log.info("Delete Category ID : " + categoryId);
        } else {
            log.info("Category ID Not Existed");
            throw new CategoryNotFoundException(categoryId);
        }
    }

    /**
     * javadoc.
     *
     * @param request create,update dto 는 CategoryCUDto 인터페이스의 구현체
     *                생성과 수정 모두 부모 카테고리가 존재하여야 하고 dto를 이용해 빌드 후
     *                JpaRepository.save() 메소드를 사용하기 때문에 하나의 메소드로 통합하였음.
     */
    private void taskForCategoryFromDto(CategoryCURequest request) {
        Category parent = categoryRepository.findById(request.getParentCategoryId())
            .orElseThrow(() -> new CategoryNotFoundException(request.getId()));

        Category category = Category.builder()
            .id(request.getId())
            .parent(parent)
            .name(request.getName())
            .depth(parent.getDepth() + 1L)
            .isExposure(request.getIsExposure())
            .build();

        categoryRepository.save(category);
    }

}
