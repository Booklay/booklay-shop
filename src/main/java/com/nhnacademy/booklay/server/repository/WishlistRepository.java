package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Wishlist.Pk> {
}
