package com.nhnacademy.booklay.server.service.board.cache;

import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;

public interface PostResponsePageCacheWrapService {

    Page<PostResponse> cacheRetrievePostResponsePage(Long productId, Pageable pageable);

    @Scheduled(cron = "0/1 * * * * *")
    void updateCheck();
}
