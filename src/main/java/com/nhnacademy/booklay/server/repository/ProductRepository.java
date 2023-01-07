package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
