package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductRepositoryCustom {

  RetrieveProductBookResponse findProductBookDataByProductId(Long id);

  RetrieveProductSubscribeResponse findProductSubscribeDataByProductId(Long id);

  List<Long> findCategoryIdsByProductId(Long id);
}
