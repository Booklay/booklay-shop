package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteProductRelationRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRelationService {

  List<RetrieveProductResponse> retrieveRecommendProducts(Long productId) throws IOException;

  List<Long> retrieveRecommendProductIds(Long productId);

  Page<RetrieveProductResponse> retrieveRecommendConnection(Long productNo, Pageable pageable)
      throws IOException;

  void createProductRelation(CreateDeleteProductRelationRequest request);

  void deleteProductRelation(CreateDeleteProductRelationRequest request);
}
