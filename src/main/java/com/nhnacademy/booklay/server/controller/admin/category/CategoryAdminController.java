package com.nhnacademy.booklay.server.controller.admin.category;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.exception.controller.CreateFailedException;
import com.nhnacademy.booklay.server.exception.controller.DeleteFailedException;
import com.nhnacademy.booklay.server.exception.controller.UpdateFailedException;
import com.nhnacademy.booklay.server.exception.service.AlreadyExistedException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.service.category.CategoryService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @GetMapping("/test")
    public ResponseEntity<CategoryResponse> testGet() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new CategoryResponse(Category.builder()
                .id(1L)
                .parent(null)
                .depth(1L)
                .name("name")
                .isExposure(false)
                .build()));
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
            throw new CreateFailedException("카테고리 생성 실패, 부모 카테고리가 없습니다.");
        } catch (AlreadyExistedException e) {
            log.error("카테고리 생성 실패, 이미 존재하는 카테고리 입니다.");
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
            throw new UpdateFailedException("카테고리 수정 실패");
        }

        CategoryResponse categoryResponse = categoryService.retrieveCategory(categoryId);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(categoryResponse);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("{\"result\": \"Success\"}");
        } catch (NotFoundException e) {
            throw new DeleteFailedException("카테고리 삭제 실패");

        }
    }

}
