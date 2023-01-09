package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.dto.category.CategoryCreateDto;
import com.nhnacademy.booklay.server.dto.category.CategoryDto;
import com.nhnacademy.booklay.server.dto.category.CategoryUpdateDto;
import com.nhnacademy.booklay.server.exception.category.ValidationFailedException;
import com.nhnacademy.booklay.server.service.category.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryAdminController {

    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String testMapping() {
        return "{\"Test Result\": \"Success\"}";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto registerCategory(@Valid @RequestBody CategoryCreateDto createDto,
                                        BindingResult bindingResult) {

        log.info("Category Create");

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        } else {
            categoryService.createCategory(createDto);
            return categoryService.retrieveCategory(createDto.getId());
        }
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<CategoryDto> getCategoryList(Pageable pageable) {
        return categoryService.retrieveCategory(pageable);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@RequestParam("categoryId") Long id) {

        log.info("Category Retrieve");

        return categoryService.retrieveCategory(id);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CategoryDto modifyCategory(@Valid @RequestBody CategoryUpdateDto updateDto,
                                      BindingResult bindingResult) {

        log.info("Category Update");

        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }

        categoryService.updateCategory(updateDto);

        return categoryService.retrieveCategory(updateDto.getId());
    }

    @GetMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteCategory(@RequestParam("categoryId") Long id) {

        log.info("Category Delete");

        if (categoryService.deleteCategory(id)) {
            return "{\"result\": \"Success\"}";
        } else {
            return "{\"result\": \"Fail\"}";
        }
    }

}