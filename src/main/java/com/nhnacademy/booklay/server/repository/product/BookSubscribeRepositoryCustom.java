package com.nhnacademy.booklay.server.repository.product;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BookSubscribeRepositoryCustom {
    List<Long> findBooksProductIdBySubscribeId(Long subscribeId);
}
