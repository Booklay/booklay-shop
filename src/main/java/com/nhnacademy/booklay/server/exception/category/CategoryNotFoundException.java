package com.nhnacademy.booklay.server.exception.category;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(long categoryId) {
        super("Category Not Found : " + categoryId);
    }
}
