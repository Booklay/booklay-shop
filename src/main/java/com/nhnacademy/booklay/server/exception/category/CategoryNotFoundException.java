package com.nhnacademy.booklay.server.exception.category;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long categoryId) {
        super("Category Not Found, Category ID : " + categoryId);
    }
}
