package com.nhnacademy.booklay.server.service.mypage.impl;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.RestockingNotificationRepository;
import com.nhnacademy.booklay.server.service.mypage.RestockingNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RestockingNotificationServiceImpl implements RestockingNotificationService {

  private final RestockingNotificationRepository restockingNotificationRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  @Override
  public void createAlarm(CreateDeleteWishlistAndAlarmRequest request) {
    Product product = productRepository.findById(request.getProductId()).orElseThrow(()->new NotFoundException(Product.class, "product not found"));
    Member member = memberRepository.findById(request.getMemberNo()).orElseThrow(()->new NotFoundException(Member.class, "member not found"));
    RestockingNotification.Pk pk = new RestockingNotification.Pk(request.getMemberNo(),
        request.getProductId());

    RestockingNotification notification = RestockingNotification.builder()
        .pk(pk)
        .product(product)
        .member(member)
        .build();

    restockingNotificationRepository.save(notification);
  }

  @Override
  public void deleteAlarm(CreateDeleteWishlistAndAlarmRequest request) {
    alarmPkValidation(request);
    RestockingNotification.Pk pk = new RestockingNotification.Pk(request.getMemberNo(),
        request.getProductId());

    if (!restockingNotificationRepository.existsById(pk)) {
      throw new NotFoundException(RestockingNotification.Pk.class, "RestockingNotification not found");
    }

    restockingNotificationRepository.deleteById(pk);
  }


  public void alarmPkValidation(CreateDeleteWishlistAndAlarmRequest request) {
    if (!memberRepository.existsById(request.getMemberNo())) {
      throw new NotFoundException(Member.class, "member not found");
    }
    if (!productRepository.existsById(request.getProductId())) {
      throw new NotFoundException(Product.class, "product not found");
    }
  }
}
