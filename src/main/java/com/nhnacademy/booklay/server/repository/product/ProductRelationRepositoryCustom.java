package com.nhnacademy.booklay.server.repository.product;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductRelationRepositoryCustom {

  List<Long> findRecommendIdsByBaseProductId(Long productId);

  Boolean existsByBaseAndTargetId(Long baseId, Long targetId);

  void deleteByBaseAndTargetId(Long baseId, Long targetId);
}
