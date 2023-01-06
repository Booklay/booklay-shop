package com.nhnacademy.booklay.server.repository;


import com.nhnacademy.booklay.server.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * javadoc. 카테고리 JPA 리포지토리
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
