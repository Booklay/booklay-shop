package com.nhnacademy.booklay.server.exception.category;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCURequest;

public class CreateCategoryFailedException extends RuntimeException {
    public CreateCategoryFailedException(CategoryCURequest request) {
        super("Failed to Create Category \n"
            + "   ID : " + request.getId() + "\n"
            + " Name : " + request.getName());
    }
}
