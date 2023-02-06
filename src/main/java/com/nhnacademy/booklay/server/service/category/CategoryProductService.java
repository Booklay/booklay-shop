package com.nhnacademy.booklay.server.service.category;

import java.util.List;
import org.springframework.util.MultiValueMap;

public interface CategoryProductService {
    List<Long> retrieveCategoryIdListByProductId(Long productId);

    MultiValueMap<Long, Long> retrieveCategoryIdListMultiValueMapByProductIdList(
        List<Long> productIdList);
}
