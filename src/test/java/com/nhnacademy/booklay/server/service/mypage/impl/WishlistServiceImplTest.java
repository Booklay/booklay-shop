package com.nhnacademy.booklay.server.service.mypage.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Wishlist;
import com.nhnacademy.booklay.server.entity.Wishlist.Pk;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.mypage.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

  CreateDeleteWishlistAndAlarmRequest request;
  CreateUpdateProductBookRequest bookRequest;
  Product product;
  Member member;

  @BeforeEach
  void setUp() {
    request = new CreateDeleteWishlistAndAlarmRequest(1L, 1L);
    bookRequest = DummyCart.getDummyProductBookDto();

    product = DummyCart.getDummyProduct(bookRequest);
    ReflectionTestUtils.setField(product, "id", 1L);
    member = Dummy.getDummyMember();
    ReflectionTestUtils.setField(member, "memberNo", 1L);
  }

  @Test
  void createWishlist() {
    given(memberRepository.existsById(request.getMemberNo())).willReturn(true);
    given(productRepository.existsById(request.getProductId())).willReturn(true);

    Wishlist.Pk pk = new Pk(request.getMemberNo(), request.getProductId());

    wishlistService.createWishlist(request);

    BDDMockito.then(wishlistRepository).should().save(any());
  }

  @Test
  void deleteWishlist() {
    //given
    given(memberRepository.existsById(request.getMemberNo())).willReturn(true);
    given(productRepository.existsById(request.getProductId())).willReturn(true);

    Wishlist.Pk pk = new Pk(request.getMemberNo(), request.getProductId());

    given(wishlistRepository.existsById(pk)).willReturn(true);

    //when
    wishlistService.deleteWishlist(request);

    BDDMockito.then(wishlistRepository).should().deleteById(pk);
  }
}