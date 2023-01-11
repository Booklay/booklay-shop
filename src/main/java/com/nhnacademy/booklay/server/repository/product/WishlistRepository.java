package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Wishlist.Pk> {

}
