package com.nhnacademy.booklay.server.exception.category;

public class CategoryAlreadyExistedException extends RuntimeException {
    public CategoryAlreadyExistedException(Long categoryId) {
        super("Category Already Existed, Category ID : " + categoryId);
    }
}
