package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.ProductTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository
    extends JpaRepository<ProductTag, ProductTag.Pk>, ProductTagRepositoryCustom {

    void deleteByPk_TagId(Long id);

    boolean existsByPk_TagId(Long id);

    List<ProductTag> findAllByPk_ProductId(Long id);


}
