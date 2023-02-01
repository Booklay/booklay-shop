package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductRepositoryCustom {

    RetrieveProductBookResponse findProductBookDataByProductId(Long id);

    RetrieveProductSubscribeResponse findProductSubscribeDataByProductId(Long id);

    List<Long> findCategoryIdsByProductId(Long id);

    Page<RetrieveBookForSubscribeResponse> findAllBooksForSubscribeBy(Pageable pageable);

    List<String> findAuthorNameByProductId(Long productId);

    Page<RetrieveProductResponse> retrieveProductPageByIds(List<Long> ids, Pageable pageable);

    RetrieveProductSubscribeResponse retrieveProductSubscribeResponseById(Long id);

    RetrieveProductBookResponse retrieveProductBookResponse(Long id);
}
