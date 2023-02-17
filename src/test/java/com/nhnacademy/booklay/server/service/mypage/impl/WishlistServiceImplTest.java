package com.nhnacademy.booklay.server.service.mypage.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.mypage.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.server.dto.product.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import com.nhnacademy.booklay.server.entity.Wishlist;
import com.nhnacademy.booklay.server.entity.Wishlist.Pk;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.mypage.RestockingNotificationRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.mypage.WishlistRepository;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {
//TODO : 실패 태스트 작성할것

  @InjectMocks
  WishlistServiceImpl wishlistService;

  @Mock
  WishlistRepository wishlistRepository;
  @Mock
  ProductRepository productRepository;
  @Mock
  MemberRepository memberRepository;
  @Mock
  RestockingNotificationRepository restockingNotificationRepository;
  @Mock
  ProductService productService;

  WishlistAndAlarmRequest request;
  CreateUpdateProductBookRequest bookRequest;
  Product product;
  Member member;
  Wishlist.Pk pk;

  @BeforeEach
  void setUp() {
    request = new WishlistAndAlarmRequest(1L, 1L);
    bookRequest = DummyCart.getDummyProductBookDto();

    product = DummyCart.getDummyProduct(bookRequest);
    ReflectionTestUtils.setField(product, "id", 1L);
    member = Dummy.getDummyMember();
    ReflectionTestUtils.setField(member, "memberNo", 1L);
    pk = new Pk(request.getMemberNo(), request.getProductId());
  }

  @Test
  void createWishlist() {
    given(memberRepository.existsById(request.getMemberNo())).willReturn(true);
    given(productRepository.existsById(request.getProductId())).willReturn(true);

    wishlistService.createWishlist(request);

    BDDMockito.then(wishlistRepository).should().save(any());
  }

  @Test
  void deleteWishlist() {
    //given
    given(memberRepository.existsById(request.getMemberNo())).willReturn(true);
    given(productRepository.existsById(request.getProductId())).willReturn(true);

    given(wishlistRepository.existsById(pk)).willReturn(true);

    //when
    wishlistService.deleteWishlist(request);

    BDDMockito.then(wishlistRepository).should().deleteById(pk);
  }

  @Test
  void deleteWishlist_failureWishlistNotExist(){
    given(memberRepository.existsById(request.getMemberNo())).willReturn(true);
    given(productRepository.existsById(request.getProductId())).willReturn(true);

    given(wishlistRepository.existsById(pk)).willReturn(false);

    assertThatThrownBy(() -> wishlistService.deleteWishlist(request))
        .isInstanceOf(NotFoundException.class);

  }

  @Test
  void wishlistPkValidation_failureMemberThenThrowException() {
    given(memberRepository.existsById(request.getMemberNo())).willReturn(false);

    assertThatThrownBy(() -> wishlistService.wishlistPkValidation(request))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  void wishlistPkValidation_failureProductThenThrowException() {
    given(memberRepository.existsById(request.getMemberNo())).willReturn(true);
    given(productRepository.existsById(request.getProductId())).willReturn(false);

    assertThatThrownBy(() -> wishlistService.wishlistPkValidation(request))
        .isInstanceOf(NotFoundException.class);
  }
  @Test
  void retrieveExists(){
    //given
    WishlistAndAlarmRequest booleanRequest = new WishlistAndAlarmRequest(1L,1L);

    Wishlist.Pk wishlistPk = new Pk(booleanRequest.getMemberNo(), booleanRequest.getProductId());
    RestockingNotification.Pk alarmPk = new RestockingNotification.Pk(booleanRequest.getMemberNo(),
        booleanRequest.getProductId());

    given(wishlistRepository.existsById(wishlistPk)).willReturn(true);
    given(restockingNotificationRepository.existsById(alarmPk)).willReturn(false);


    //when
    WishlistAndAlarmBooleanResponse result = wishlistService.retrieveExists(booleanRequest);

    //then
    assertThat(result.getWishlist()).isTrue();
    assertThat(result.getAlarm()).isFalse();
  }

  @Test
  void retrievePage() throws IOException {
    List<Wishlist> alarms = new ArrayList<>();
    alarms.add(new Wishlist(pk));
    Pageable pageable = Pageable.ofSize(10);
    Page<Wishlist> wishlistPage = new PageImpl<>(alarms, pageable, 1);

    given(wishlistRepository.retrieveRegister(member.getMemberNo(),
        pageable)).willReturn(wishlistPage);

    List<Long> productIds = wishlistPage.getContent().stream()
        .map(wishlist->wishlist.getPk().getProductId())
        .collect(Collectors.toList());

    List<RetrieveProductResponse> content = new ArrayList<>();
    content.add(new RetrieveProductResponse(product,
        DummyCart.getDummySubscribe(DummyCart.getDummyProductSubscribeDto())));
    given(productService.retrieveProductResponses(productIds)).willReturn(content);

    //when
    Page<RetrieveProductResponse> result = wishlistService.retrievePage(
        member.getMemberNo(), pageable);

    //then
    assertThat(result.getTotalElements()).isEqualTo(content.size());
  }

  @Test
  void retrieveWishlist() throws IOException {
    Long memberNo = 1L;
    int LIMIT = 5;
    List<Wishlist> wishs = new ArrayList<>();
    wishs.add(new Wishlist(pk));

    given(wishlistRepository.retrieveWishlist(memberNo, LIMIT)).willReturn(wishs);

    List<Long> productIds = wishs.stream()
        .map(wishlist -> wishlist.getPk().getProductId())
        .collect(Collectors.toList());

    wishlistService.retrieveWishlist(memberNo);

    BDDMockito.then(productService).should().retrieveProductResponses(productIds);
  }
}
