package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    <T> Page<T> findAllBy(Pageable pageable, Class<T> type);

    boolean existsByName(String name);
}
