package com.nhnacademy.booklay.server.service.mypage.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.mypage.RestockingNotificationRepository;
import java.util.Optional;
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
class RestockingNotificationServiceImplTest {
//TODO : 실패 태스트 작성할것
  @InjectMocks
  RestockingNotificationServiceImpl restockingNotificationService;
  @Mock
  RestockingNotificationRepository restockingNotificationRepository;
  @Mock
  MemberRepository memberRepository;
  @Mock
  ProductRepository productRepository;

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
  void createAlarm() {
    RestockingNotification.Pk pk = new RestockingNotification.Pk(request.getMemberNo(),
        request.getProductId());

    given(productRepository.findById(request.getProductId())).willReturn(
        Optional.ofNullable(product));
    given(memberRepository.findById(request.getMemberNo())).willReturn(Optional.ofNullable(member));

    RestockingNotification restockingNotification = RestockingNotification.builder()
        .member(member)
        .product(product)
        .pk(pk)
        .build();

    restockingNotificationService.createAlarm(request);

    BDDMockito.then(restockingNotificationRepository).should().save(any());

  }

  @Test
  void deleteAlarm() {
    //given
    given(memberRepository.existsById(request.getMemberNo())).willReturn(true);
    given(productRepository.existsById(request.getProductId())).willReturn(true);

    RestockingNotification.Pk pk = new RestockingNotification.Pk(request.getMemberNo(),
        request.getProductId());

    given(restockingNotificationRepository.existsById(pk)).willReturn(true);

    //when
    restockingNotificationService.deleteAlarm(request);

    //then
    BDDMockito.then(restockingNotificationRepository).should().deleteById(pk);
  }
}