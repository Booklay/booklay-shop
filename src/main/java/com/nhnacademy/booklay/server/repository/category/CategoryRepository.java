package com.nhnacademy.booklay.server.repository.category;


import com.nhnacademy.booklay.server.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * javadoc. 카테고리 JPA 리포지토리
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    <T> Page<T> findAllBy(Pageable pageable, Class<T> type);

    @Modifying
    @Query("update Category ca set ca.parent.id = :changeId where ca.parent.id = :existId")
    int updateParentCategory(@Param("existId") Long existId, @Param("changeId") Long changeId);

    @Modifying
    @Query("update CategoryProduct ca set ca.pk.categoryId = :changeId where ca.pk.categoryId = :existId")
    int updateProductCategory(@Param("existId") Long existId, @Param("changeId") Long changeId);

    @Modifying
    @Query("update Coupon c set c.category.id = :changeId where c.category.id = :existId")
    int updateCoupon(@Param("existId") Long existId, @Param("changeId") Long changeId);

}
