package com.nhnacademy.booklay.server.exception.category;

import com.nhnacademy.booklay.server.dto.category.CategoryCUDto;

public class CreateCategoryFailedException extends RuntimeException {
    public CreateCategoryFailedException(CategoryCUDto dto) {
        super("Failed to Create Category \n"
            + "   ID : " + dto.getId() + "\n"
            + " Name : " + dto.getName());
    }
}
