package com.nhnacademy.booklay.server.service.mypage.impl;

import com.nhnacademy.booklay.server.dto.product.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.mypage.RestockingNotificationRepository;
import com.nhnacademy.booklay.server.service.mypage.RestockingNotificationService;
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
public class RestockingNotificationServiceImpl implements RestockingNotificationService {

  private final RestockingNotificationRepository restockingNotificationRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  private final ProductService productService;

  @Override
  public Page<RetrieveProductResponse> retrievePage(Long memberId, Pageable pageable) throws IOException {
    Page<RestockingNotification> notificationsPage =restockingNotificationRepository.retrieveRegister(memberId, pageable);

    List<Long> productIds = new ArrayList<>();
    for(int i=0; i< notificationsPage.getContent().size(); i++){
      productIds.add(notificationsPage.getContent().get(i).getProduct().getId());
    }

    List<RetrieveProductResponse> content = productService.retrieveProductResponses(productIds);

    return new PageImpl<>(content, pageable, notificationsPage.getTotalElements());
  }

  @Override
  public void createAlarm(WishlistAndAlarmRequest request) {
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
  public void deleteAlarm(WishlistAndAlarmRequest request) {
    alarmPkValidation(request);
    RestockingNotification.Pk pk = new RestockingNotification.Pk(request.getMemberNo(),
        request.getProductId());

    if (!restockingNotificationRepository.existsById(pk)) {
      throw new NotFoundException(RestockingNotification.Pk.class, "RestockingNotification not found");
    }

    restockingNotificationRepository.deleteById(pk);
  }


  public void alarmPkValidation(WishlistAndAlarmRequest request) {
    if (!memberRepository.existsById(request.getMemberNo())) {
      throw new NotFoundException(Member.class, "member not found");
    }
    if (!productRepository.existsById(request.getProductId())) {
      throw new NotFoundException(Product.class, "product not found");
    }
  }
}
