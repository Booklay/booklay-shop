package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.util.List;

public interface ProductRelationService {

  List<RetrieveProductResponse> retrieveRecommendProducts(Long productId);

}
