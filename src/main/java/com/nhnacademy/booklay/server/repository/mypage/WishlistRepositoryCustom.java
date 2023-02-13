package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.entity.Wishlist;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WishlistRepositoryCustom {
  Page<Wishlist> retrieveRegister(Long memberId, Pageable pageable);

  List<Wishlist> retrieveWishlist(Long memberId, Integer limit);
}
