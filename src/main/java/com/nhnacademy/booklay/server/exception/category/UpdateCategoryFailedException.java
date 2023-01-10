package com.nhnacademy.booklay.server.exception.category;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCURequest;

public class UpdateCategoryFailedException extends RuntimeException {
    public UpdateCategoryFailedException(CategoryCURequest request) {
        super("Failed to Update Category \n"
            + "   ID : " + request.getId() + "\n"
            + " Name : " + request.getName());
    }
}
