package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Cart;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class CartRepositoryTest {

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  CartRepository cartRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  ProductRepository productRepository;


  @Test
  void testCategorySave() {
    Cart cart = DummyCart.getDummyCart();

    entityManager.persist(cart.getMember().getGender());
    memberRepository.save(cart.getMember());

    entityManager.persist(cart.getProduct().getImage());
    productRepository.save(cart.getProduct());

    Cart expected = cartRepository.save(cart);

    assertThat(expected.getPk()).isEqualTo(cart.getPk());
  }
}