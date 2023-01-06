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

  @Test
  void testCategorySave() {
    //given
    Cart cart = DummyCart.getDummyCart();
    entityManager.persist(cart);

    //when
    Cart expected = cartRepository.save(cart);

    //then
    assertThat(expected.getPk()).isEqualTo(cart.getPk());
  }
}