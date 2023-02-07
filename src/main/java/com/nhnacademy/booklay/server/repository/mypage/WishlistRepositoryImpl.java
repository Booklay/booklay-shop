package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.entity.QWishlist;
import com.nhnacademy.booklay.server.entity.Wishlist;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

public class WishlistRepositoryImpl extends QuerydslRepositorySupport implements
    WishlistRepositoryCustom {

  public WishlistRepositoryImpl() {
    super(Wishlist.class);
  }

  @Override
  public Page<Wishlist> retrieveRegister(Long memberId, Pageable pageable) {
    QWishlist wishlist = QWishlist.wishlist;

    List<Wishlist> content = from(wishlist).where(wishlist.pk.memberId.eq(memberId))
        .select(wishlist)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPQLQuery<Long> count = from(wishlist)
        .select(wishlist.count());
    return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
  }
}
