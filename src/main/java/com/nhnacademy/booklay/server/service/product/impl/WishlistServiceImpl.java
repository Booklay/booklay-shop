package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.request.CreateWishlistRequest;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Wishlist;
import com.nhnacademy.booklay.server.entity.Wishlist.Pk;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.WishlistRepository;
import com.nhnacademy.booklay.server.service.product.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

  private final WishlistRepository wishlistRepository;
  private final ProductRepository productRepository;
  private final MemberRepository memberRepository;

  @Override
  public void createWishlist(CreateWishlistRequest request) {
    wishlistValidation(request);

    Wishlist.Pk pk = new Pk(request.getMemberId(), request.getProductId());

    Wishlist wishlist = Wishlist.builder()
        .pk(pk)
        .build();

    wishlistRepository.save(wishlist);
  }

  @Override
  public void deleteWishlist(CreateWishlistRequest request) {
    wishlistValidation(request);
    Wishlist.Pk pk = new Pk(request.getMemberId(), request.getProductId());

    //없는걸 지우려고하면 안되니까...
    if (!wishlistRepository.existsById(pk)) {
      throw new NotFoundException(Wishlist.Pk.class, "wishlist not found");
    }

    wishlistRepository.deleteById(pk);
  }


  public void wishlistValidation(CreateWishlistRequest request) {
    if (!memberRepository.existsById(request.getMemberId())) {
      throw new NotFoundException(Member.class, "member not found");
    }
    if (!productRepository.existsById(request.getProductId())) {
      throw new NotFoundException(Product.class, "product not found");
    }
  }
}
