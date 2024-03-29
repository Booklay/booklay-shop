package com.nhnacademy.booklay.server.controller.admin.category;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.category.response.CategoryStepResponse;
import com.nhnacademy.booklay.server.exception.controller.DeleteFailedException;
import com.nhnacademy.booklay.server.exception.service.AlreadyExistedException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.service.category.CategoryService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/steps/{categoryId}")
    public ResponseEntity<CategoryStepResponse> retrieveStep(@PathVariable Long categoryId) {

        CategoryStepResponse categoryStep = categoryService.retrieveCategoryStep(categoryId);

        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(categoryStep);

    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
        @Valid @RequestBody CategoryCreateRequest createDto) {

        log.info("Create Category");

        log.info("Request Check {}", createDto.getName());

        CategoryResponse categoryResponse;

        try {
            categoryService.createCategory(createDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .build();
        } catch (AlreadyExistedException e) {
            log.error("카테고리 생성 실패, 이미 존재하는 카테고리 번호 입니다.");
        }

        categoryResponse = categoryService.retrieveCategory(createDto.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> retrieveCategoryList(Pageable pageable) {

        PageResponse<CategoryResponse> page = categoryService.retrieveCategory(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(page);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> retrieveCategory(@PathVariable("categoryId") Long id) {
        CategoryResponse categoryResponse = categoryService.retrieveCategory(id);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(categoryResponse);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
        @PathVariable(value = "categoryId") Long categoryId,
        @Valid @RequestBody CategoryUpdateRequest updateDto) {
        try {
            categoryService.updateCategory(updateDto, categoryId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .build();
        }

        CategoryResponse categoryResponse = categoryService.retrieveCategory(updateDto.getId());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(categoryResponse);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("{\"result\": \"Success\"}");
        } catch (NotFoundException e) {
            throw new DeleteFailedException("카테고리 삭제 실패");

        }
    }

}
