package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Wishlist.Pk>, WishlistRepositoryCustom {
  Boolean existsByPk_MemberId(Long memberId);
}
