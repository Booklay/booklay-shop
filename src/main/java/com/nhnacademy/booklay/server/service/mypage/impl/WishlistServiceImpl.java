package com.nhnacademy.booklay.server.service.mypage.impl;

import com.nhnacademy.booklay.server.dto.mypage.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import com.nhnacademy.booklay.server.entity.Wishlist;
import com.nhnacademy.booklay.server.entity.Wishlist.Pk;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.mypage.RestockingNotificationRepository;
import com.nhnacademy.booklay.server.repository.mypage.WishlistRepository;
import com.nhnacademy.booklay.server.service.mypage.WishlistService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
  private final RestockingNotificationRepository restockingNotificationRepository;
  private final ProductRepository productRepository;
  private final MemberRepository memberRepository;

  private final ProductService productService;

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

  @Override
  public WishlistAndAlarmBooleanResponse retrieveExists(Long memberNo) {
    return new WishlistAndAlarmBooleanResponse(wishlistRepository.existsByPk_MemberId(memberNo),
        restockingNotificationRepository.existsByPk_MemberId(memberNo));
  }

  @Override
  public Page<RetrieveProductResponse> retrievePage(Long memberNo, Pageable pageable)
      throws IOException {
    Page<Wishlist> wishlistPage =wishlistRepository.retrieveRegister(memberNo, pageable);

    List<Long> productIds = new ArrayList<>();
    for(int i=0; i< wishlistPage.getContent().size(); i++){
      productIds.add(wishlistPage.getContent().get(i).getPk().getProductId());
    }

    List<RetrieveProductResponse> content = productService.retrieveProductResponses(productIds);
    for(RetrieveProductResponse product : content){
      product.setAlarm(restockingNotificationRepository.existsByPk_MemberId(memberNo));
    }

    for(RetrieveProductResponse product : content) {
      log.info(product.getAlarm() + "출력");
    }

    return new PageImpl<>(content, pageable, wishlistPage.getTotalElements());
  }
}
