package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.repository.CategoryRepository;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * javadoc. 카테고리 서비스
 */
@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
