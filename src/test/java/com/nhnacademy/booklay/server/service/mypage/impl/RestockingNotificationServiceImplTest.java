package com.nhnacademy.booklay.server.service.mypage.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.product.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import com.nhnacademy.booklay.server.entity.RestockingNotification.Pk;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.mypage.RestockingNotificationRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
class RestockingNotificationServiceImplTest {
  @InjectMocks
  RestockingNotificationServiceImpl restockingNotificationService;
  @Mock
  RestockingNotificationRepository restockingNotificationRepository;
  @Mock
  MemberRepository memberRepository;
  @Mock
  ProductRepository productRepository;
  @Mock
  ProductService productService;

  WishlistAndAlarmRequest request;
  CreateUpdateProductBookRequest bookRequest;
  Product product;
  Member member;
  RestockingNotification.Pk pk;

  @BeforeEach
  void setUp() {
    request = new WishlistAndAlarmRequest(1L, 1L);
    bookRequest = DummyCart.getDummyProductBookDto();

    product = DummyCart.getDummyProduct(bookRequest);
    ReflectionTestUtils.setField(product, "id", 1L);
    member = Dummy.getDummyMember();
    ReflectionTestUtils.setField(member, "memberNo", 1L);
    pk = new Pk(member.getMemberNo(), product.getId());
  }

  @Test
  void retrievePage() throws IOException {
    List<RestockingNotification> alarms = new ArrayList<>();
    alarms.add(new RestockingNotification(pk, member, product));
    Pageable pageable = Pageable.ofSize(10);
    Page<RestockingNotification> notificationsPage = new PageImpl<>(alarms, pageable, 1);

    given(restockingNotificationRepository.retrieveRegister(member.getMemberNo(),
        pageable)).willReturn(notificationsPage);

    List<Long> productIds = new ArrayList<>();
    for (int i = 0; i < notificationsPage.getContent().size(); i++) {
      productIds.add(notificationsPage.getContent().get(i).getProduct().getId());
    }

    List<RetrieveProductResponse> content = new ArrayList<>();
    content.add(new RetrieveProductResponse(product,
        DummyCart.getDummySubscribe(DummyCart.getDummyProductSubscribeDto())));
    given(productService.retrieveProductResponses(productIds)).willReturn(content);

    //when
    Page<RetrieveProductResponse> result = restockingNotificationService.retrievePage(
        member.getMemberNo(), pageable);

    //then
    assertThat(result.getTotalElements()).isEqualTo(content.size());
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

  @Test
  void deleteAlarm_failureAlarmNotExist(){
    given(memberRepository.existsById(request.getMemberNo())).willReturn(true);
    given(productRepository.existsById(request.getProductId())).willReturn(true);

    RestockingNotification.Pk pk = new RestockingNotification.Pk(request.getMemberNo(),
        request.getProductId());

    given(restockingNotificationRepository.existsById(pk)).willReturn(false);

    assertThatThrownBy(() -> restockingNotificationService.deleteAlarm(request))
        .isInstanceOf(NotFoundException.class);

  }

  @Test
  void alarmPkValidation_failureMemberThenThrowException() {
    given(memberRepository.existsById(request.getMemberNo())).willReturn(false);

    assertThatThrownBy(() -> restockingNotificationService.alarmPkValidation(request))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  void alarmPkValidation_failureProductThenThrowException() {
    given(memberRepository.existsById(request.getMemberNo())).willReturn(true);
    given(productRepository.existsById(request.getProductId())).willReturn(false);

    assertThatThrownBy(() -> restockingNotificationService.alarmPkValidation(request))
        .isInstanceOf(NotFoundException.class);
  }
}