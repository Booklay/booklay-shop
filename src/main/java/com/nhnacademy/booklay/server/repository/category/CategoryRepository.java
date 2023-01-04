package com.nhnacademy.booklay.server.repository.category;

import com.nhnacademy.booklay.server.entitiy.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * javadoc. 카테고리 JPA 리포지토리
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
