package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findTop8ByIsDeletedOrderByCreatedAtDesc(boolean isDeleted);
    List<Product> findTop8ByIsDeletedIsFalseOrderByCreatedAtDesc();

    Page<Product> findAllByIsDeletedOrderByCreatedAtDesc(boolean isDeleted, Pageable pageable);
    Page<Product> findByIdInAndIsDeletedOrderByCreatedAtDesc(List<Long> ids, boolean isDeleted, Pageable pageable);

    <T> Page<T> findAllBy(Pageable pageable, Class<T> type);
}
