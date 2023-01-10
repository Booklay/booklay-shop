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
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryCreateRequest createDto) {
        try {
            categoryService.createCategory(createDto);
        } catch (AlreadyExistedException | NotFoundException e) {
            throw new CreateFailedException("카테고리 생성 실패");
        }
        return categoryService.retrieveCategory(createDto.getId());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CategoryResponse> retrieveCategoryList(Pageable pageable) {
        return categoryService.retrieveCategory(pageable);
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse retrieveCategory(@PathVariable("categoryId") Long id) {
        return categoryService.retrieveCategory(id);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CategoryResponse updateCategory(@PathVariable(value = "categoryId") Long categoryId,
                                           @Valid @RequestBody CategoryUpdateRequest updateDto) {
        try {
            categoryService.updateCategory(updateDto, categoryId);
        } catch (NotFoundException e) {
            throw new UpdateFailedException("카테고리 수정 실패");
        }
        return categoryService.retrieveCategory(updateDto.getId());
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteCategory(@PathVariable("categoryId") Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return "{\"result\": \"Success\"}";
        } catch (NotFoundException e) {
            throw new DeleteFailedException("카테고리 삭제 실패");
        }
    }

}
