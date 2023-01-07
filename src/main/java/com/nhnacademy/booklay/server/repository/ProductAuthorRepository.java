package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.ProductAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAuthorRepository extends JpaRepository<ProductAuthor, ProductAuthor.Pk> {

}
