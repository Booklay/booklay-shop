package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

  boolean existsSubscribeByProduct(Product product);

  Subscribe findSubscribeByProduct(Product product);
}
