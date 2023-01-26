package com.nhnacademy.booklay.server.repository.category.impl;

import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.repository.category.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CategoryRepositoryImpl extends QuerydslRepositorySupport
    implements CategoryRepositoryCustom {
    public CategoryRepositoryImpl() {
        super(Category.class);
    }
}
