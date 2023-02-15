package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Review;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewsByProductNoAndIsDeleted(Long productId, Boolean isDeleted, Pageable pageable);

    boolean existsByProductNoAndMemberNo(Long productId, Long memberNo);

    @Modifying
    @Query("update Review r set r.isDeleted = true where r.id = ?1 ")
    void softDeleteReview(@Param("reviewId") Long reviewId);

}
