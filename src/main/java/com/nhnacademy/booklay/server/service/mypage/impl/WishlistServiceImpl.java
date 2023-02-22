package com.nhnacademy.booklay.server.service.mypage.impl;

import com.nhnacademy.booklay.server.dto.mypage.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.server.dto.product.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import com.nhnacademy.booklay.server.entity.Wishlist;
import com.nhnacademy.booklay.server.entity.Wishlist.Pk;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.mypage.RestockingNotificationRepository;
import com.nhnacademy.booklay.server.repository.mypage.WishlistRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.mypage.WishlistService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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
    private final Integer LIMIT = 5;

  @Override
  public void createWishlist(WishlistAndAlarmRequest request) {
    wishlistPkValidation(request);

    Wishlist.Pk pk = new Pk(request.getMemberNo(), request.getProductId());

    Wishlist wishlist = Wishlist.builder()
        .pk(pk)
        .build();

    wishlistRepository.save(wishlist);
  }

  @Override
  public void deleteWishlist(WishlistAndAlarmRequest request) {
    wishlistPkValidation(request);
    Wishlist.Pk pk = new Pk(request.getMemberNo(), request.getProductId());

    if (!wishlistRepository.existsById(pk)) {
      throw new NotFoundException(Wishlist.Pk.class, "wishlist not found");
    }
    wishlistRepository.deleteById(pk);
  }


  public void wishlistPkValidation(WishlistAndAlarmRequest request) {
    if (!memberRepository.existsById(request.getMemberNo())) {
      throw new NotFoundException(Member.class, "member not found");
    }
    if (!productRepository.existsById(request.getProductId())) {
      throw new NotFoundException(Product.class, "product not found");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public WishlistAndAlarmBooleanResponse retrieveExists(WishlistAndAlarmRequest request) {
    Wishlist.Pk wishlistPk = new Pk(request.getMemberNo(), request.getProductId());
    RestockingNotification.Pk alarmPk = new RestockingNotification.Pk(request.getMemberNo(),
        request.getProductId());
    WishlistAndAlarmBooleanResponse response =
        new WishlistAndAlarmBooleanResponse(
            wishlistRepository.existsById(wishlistPk),
            restockingNotificationRepository.existsById(alarmPk));

    log.info("위시리스트 " + response.getWishlist() + "재입고 알림 " + response.getWishlist());
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<RetrieveProductResponse> retrievePage(Long memberNo, Pageable pageable)
      throws IOException {
    Page<Wishlist> wishlistPage = wishlistRepository.retrieveRegister(memberNo, pageable);

    List<Long> productIds = wishlistPage.getContent().stream()
        .map(wishlist -> wishlist.getPk().getProductId())
        .collect(Collectors.toList());

    List<RetrieveProductResponse> content = productService.retrieveProductResponses(productIds);
    for (RetrieveProductResponse product : content) {
      RestockingNotification.Pk pk = new RestockingNotification.Pk(memberNo,
          product.getProductId());
      product.setAlarm(restockingNotificationRepository.existsById(pk));
    }

    return new PageImpl<>(content, pageable, wishlistPage.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public List<RetrieveProductResponse> retrieveWishlist(Long memberNo) throws IOException {
    List<Wishlist> wishs = wishlistRepository.retrieveWishlist(memberNo, LIMIT);

    List<Long> productIds = wishs.stream()
        .map(wishlist -> wishlist.getPk().getProductId())
        .collect(Collectors.toList());

    return productService.retrieveProductResponses(productIds);
  }
}
