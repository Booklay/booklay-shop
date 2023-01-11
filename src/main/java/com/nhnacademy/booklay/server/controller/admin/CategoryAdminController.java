package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.exception.controller.CreateFailedException;
import com.nhnacademy.booklay.server.exception.controller.DeleteFailedException;
import com.nhnacademy.booklay.server.exception.controller.UpdateFailedException;
import com.nhnacademy.booklay.server.exception.service.AlreadyExistedException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
        @Valid @RequestBody CategoryCreateRequest createDto) {
        try {
            categoryService.createCategory(createDto);
        } catch (AlreadyExistedException | NotFoundException e) {
            throw new CreateFailedException("카테고리 생성 실패");
        }

        CategoryResponse categoryResponse = categoryService.retrieveCategory(createDto.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<Page> retrieveCategoryList(Pageable pageable) {

        Page<CategoryResponse> page = categoryService.retrieveCategory(pageable);

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
