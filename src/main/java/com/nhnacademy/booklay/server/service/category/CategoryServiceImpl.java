package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.dto.category.CategoryCUDto;
import com.nhnacademy.booklay.server.dto.category.CategoryCreateDto;
import com.nhnacademy.booklay.server.dto.category.CategoryDto;
import com.nhnacademy.booklay.server.dto.category.CategoryUpdateDto;
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
    public void createCategory(CategoryCreateDto createDto) {
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
    public CategoryDto retrieveCategory(Long categoryId) {
        CategoryDto categoryDto = categoryRepository.findById(categoryId, CategoryDto.class)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        log.info("Retrieve Category : " + categoryDto.getName());

        return categoryDto;
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
            try {
                taskForCategoryFromDto(updateDto);
                log.info("Update Category Success : " + updateDto.getName());
            } catch (CategoryNotFoundException e) {
                log.info("Update Category Failed");
                throw new UpdateCategoryFailedException(updateDto);
            }
        } else {
            log.info("Category ID Not Existed");
            throw new CategoryNotFoundException(updateDto.getId());
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
     * @param dto create,update dto 는 CategoryCUDto 인터페이스의 구현체
     *            생성과 수정 모두 부모 카테고리가 존재하여야 하고 dto를 이용해 빌드 후
     *            JpaRepository.save() 메소드를 사용하기 때문에 하나의 메소드로 통합하였음.
     */
    private void taskForCategoryFromDto(CategoryCUDto dto) {
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
    }

}
