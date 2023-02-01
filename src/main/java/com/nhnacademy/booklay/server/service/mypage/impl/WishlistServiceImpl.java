package com.nhnacademy.booklay.server.service.mypage.impl;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Wishlist;
import com.nhnacademy.booklay.server.entity.Wishlist.Pk;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.WishlistRepository;
import com.nhnacademy.booklay.server.service.mypage.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    public void createWishlist(CreateDeleteWishlistAndAlarmRequest request) {
        wishlistPkValidation(request);

        Wishlist.Pk pk = new Pk(request.getMemberNo(), request.getProductId());

        Wishlist wishlist = Wishlist.builder()
                                    .pk(pk)
                                    .build();

        wishlistRepository.save(wishlist);
    }

    @Override
    public void deleteWishlist(CreateDeleteWishlistAndAlarmRequest request) {
        wishlistPkValidation(request);
        Wishlist.Pk pk = new Pk(request.getMemberNo(), request.getProductId());

        if (!wishlistRepository.existsById(pk)) {
            throw new NotFoundException(Wishlist.Pk.class, "wishlist not found");
        }

        wishlistRepository.deleteById(pk);
    }


    public void wishlistPkValidation(CreateDeleteWishlistAndAlarmRequest request) {
        if (!memberRepository.existsById(request.getMemberNo())) {
            throw new NotFoundException(Member.class, "member not found");
        }
        if (!productRepository.existsById(request.getProductId())) {
            throw new NotFoundException(Product.class, "product not found");
        }
    }
}
