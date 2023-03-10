package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    boolean existsSubscribeByProduct(Product product);

    Subscribe findSubscribeByProduct(Product product);

    List<Subscribe> findAllByProductNoIn(List<Long> productNoList);
}
