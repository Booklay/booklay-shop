package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductTagRepositoryCustom {
    List<RetrieveTagResponse> findTagsByProductId(Long id);
}
