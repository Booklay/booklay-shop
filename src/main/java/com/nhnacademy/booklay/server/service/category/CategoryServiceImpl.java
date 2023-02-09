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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

        if (Objects.nonNull(createRequest.getParentCategoryId()) && createRequest.getParentCategoryId() != 0) {
            parentCategory = categoryRepository.findById(createRequest.getParentCategoryId());
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

    /**
     *  1. 리퀘스트의 아이디와 수정 요청이 들어온 아이디를 비교
     *  2. 먼저 리퀘스트를 토대로 엔티티를 수정하고 save
     *  3. 하위 카테고리들의 상위 카테고리 정보 및 코드 변경하여 저장
     *  4. 이후 기존 요청 들어온 카테고리 정보를 삭제
     */
    @Override
    public void updateCategory(CategoryUpdateRequest updateRequest, Long categoryId) {

        Category category = categoryFindById(categoryId);

        Optional<Category> parentCategory =
            Objects.nonNull(updateRequest.getParentCategoryId())
                ? categoryRepository.findById(updateRequest.getParentCategoryId()) : Optional.empty();

        categoryRepository.save(updateRequest.toEntity(parentCategory));

        if (!categoryId.equals(updateRequest.getId())) {
            categoryRepository.delete(category);
        }
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
        MultiValueMap<Long, CategoryStepResponse> map = new LinkedMultiValueMap<>();
        List<Category> categoryList = categoryRepository.findAll();
        categoryList.forEach(category1 ->
            map.add(category1.getDepth(), new CategoryStepResponse(category1))
        );
        Long[][] depth = {{0L, 1L}, {1L, 2L}};
        for (Long[] depthArray : depth){
            List<CategoryStepResponse> categoryListLevel1 = map.get(depthArray[0]);
            assert categoryListLevel1 != null;
            Objects.requireNonNull(map.get(depthArray[1])).forEach(category1 -> {
                if (category1.getParentId() != null){
                    for(CategoryStepResponse category2 : categoryListLevel1){
                        if (Objects.equals(category2.getId(), category1.getParentId())){
                            category2.getCategories().add(category1);
                            break;
                        }
                    }
                }
            });
        }
        return Objects.requireNonNull(map.get(0L)).get(1);
    }

    private Category categoryFindById(Long categoryId) {

        return categoryRepository.findById(categoryId)
                                 .orElseThrow(() -> new NotFoundException(Category.class,
                                                                          NOT_FOUND_MESSAGE));
    }

}
