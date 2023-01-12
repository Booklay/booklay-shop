package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

  @Modifying
  @Query(value = "update Subscribe  "
      + "as s set s.subscribeDay = :#{#subscribe.subscribeDay}, "
      + "s.subscribeWeek = :#{#subscribe.subscribeWeek}, s.publisher = :#{#subscribe.publisher} "
      + "where s.id = ?1")
  Subscribe updateSubscribeById(Long id, Subscribe subscribe);
}
