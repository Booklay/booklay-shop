package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BookSubscribeRepositoryCustom {
  List<Long> findBooksProductIdBySubscribeId(Long subscribeId);
}
