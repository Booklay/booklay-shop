package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductDetailRepositoryCustom {

    List<RetrieveAuthorResponse> findAuthorsByProductDetailId(Long id);

    List<Long> findAuthorIdsByProductDetailId(Long id);
}
