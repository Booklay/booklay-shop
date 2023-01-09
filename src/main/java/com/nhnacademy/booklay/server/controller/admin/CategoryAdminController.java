package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.server.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.exception.category.CategoryNotFoundException;
import com.nhnacademy.booklay.server.exception.category.ValidationFailedException;
import com.nhnacademy.booklay.server.service.category.CategoryService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryCreateRequest createDto,
                                           BindingResult bindingResult) {

        log.info("Category Create");

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        } else {
            categoryService.createCategory(createDto);
            return categoryService.retrieveCategory(createDto.getId());
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CategoryResponse> retrieveCategoryList(Pageable pageable) {
        return categoryService.retrieveCategory(pageable);
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse retrieveCategory(@PathVariable("categoryId") Long id) {

        log.info("Category Retrieve");

        return categoryService.retrieveCategory(id);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CategoryResponse updateCategory(@PathVariable(value = "categoryId") Long categoryId,
                                           @Valid @RequestBody CategoryUpdateRequest updateDto,
                                           BindingResult bindingResult) {

        log.info("Category Update ID : {}", categoryId);

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        categoryService.updateCategory(updateDto, categoryId);

        return categoryService.retrieveCategory(updateDto.getId());
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteCategory(@PathVariable("categoryId") Long categoryId) {

        log.info("Category Delete");

        try {
            categoryService.deleteCategory(categoryId);
            return "{\"result\": \"Success\"}";
        } catch (CategoryNotFoundException e) {
            return "{\"result\": \"Fail\"}";
        }
    }

}
