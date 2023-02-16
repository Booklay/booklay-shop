package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.ProductAuthor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductAuthorRepository extends JpaRepository<ProductAuthor, ProductAuthor.Pk> {

    @Modifying
    @Query(value = "delete from ProductAuthor as pd where pd.pk.bookId=?1")
    void deleteAllByProductDetailId(Long id);

    void deleteByPk_AuthorId(Long id);

    boolean existsByPk_AuthorId(Long id);

    List<ProductAuthor> findAllByAuthor_AuthorId(Long authorId);
}
