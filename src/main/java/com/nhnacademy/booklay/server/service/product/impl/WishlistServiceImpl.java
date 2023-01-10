package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.request.CreateWishlistRequest;
import com.nhnacademy.booklay.server.entity.Wishlist;
import com.nhnacademy.booklay.server.entity.Wishlist.Pk;
import com.nhnacademy.booklay.server.repository.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.WishlistRepository;
import com.nhnacademy.booklay.server.service.product.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

  private final WishlistRepository wishlistRepository;
  private final ProductRepository productRepository;
  private final MemberRepository memberRepository;

  @Override
  public void createWishlist(CreateWishlistRequest request) {
    //TODO : 예외처리 만들것
    if(!memberRepository.existsById(request.getMemberId())){

    }
    if(!productRepository.existsById(request.getProductId())){

    }

    Wishlist.Pk pk = new Pk(request.getMemberId(), request.getProductId());

    Wishlist wishlist = Wishlist.builder()
        .pk(pk)
        .build();

    wishlistRepository.save(wishlist);
  }

  @Override
  public void deleteWishlist(CreateWishlistRequest request) {
    //TODO : 예외처리 만들것
    if(!memberRepository.existsById(request.getMemberId())){

    }
    if(!productRepository.existsById(request.getProductId())){

    }
    Wishlist.Pk pk = new Pk(request.getMemberId(), request.getProductId());

    //없는걸 지우려고함
    if(!wishlistRepository.existsById(pk)){

    }
    wishlistRepository.deleteById(pk);
  }
}
