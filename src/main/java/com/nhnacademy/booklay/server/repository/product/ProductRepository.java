package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product,Long> {
  @Modifying
  @Query("update Product as p "
      + "set p.image = :#{#product.image}, p.isSelling = :#{#product.isSelling()},"
      + " p.longDescription = :#{#product.longDescription}, p.shortDescription= :#{#product.shortDescription},"
      + " p.pointMethod= :#{#product.pointMethod}, p.pointRate= :#{#product.pointRate}, p.price = :#{#product.price},"
      + " p.title= :#{#product.title} "
      + "where p.id = ?1")
  void updateProductById(Long id, Product product);
}
