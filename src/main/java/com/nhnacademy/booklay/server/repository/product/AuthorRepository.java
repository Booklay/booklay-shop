package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    <T> Page<T> findAllBy(Pageable pageable, Class<T> type);
}
