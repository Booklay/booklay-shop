package com.nhnacademy.booklay.server.exception.category;

import com.nhnacademy.booklay.server.dto.category.CategoryCUDto;

public class UpdateCategoryFailedException extends RuntimeException {
    public UpdateCategoryFailedException(CategoryCUDto dto) {
        super("Failed to Update Category \n"
            + "   ID : " + dto.getId() + "\n"
            + " Name : " + dto.getName());
    }
}
