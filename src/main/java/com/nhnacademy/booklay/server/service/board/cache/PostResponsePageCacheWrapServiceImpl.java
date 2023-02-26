package com.nhnacademy.booklay.server.service.board.cache;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.POST_RESPONSE_PAGE_CACHE;

import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import com.nhnacademy.booklay.server.dto.product.cache.ObjectWrapDto;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.board.PostService;
import com.nhnacademy.booklay.server.utils.CacheServiceExtend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostResponsePageCacheWrapServiceImpl extends CacheServiceExtend<Page<PostResponse>> implements PostResponsePageCacheWrapService {
    private final PostService postService;

    public PostResponsePageCacheWrapServiceImpl(RedisCacheService redisCacheService, PostService postService) {
        super(redisCacheService, POST_RESPONSE_PAGE_CACHE);
        this.postService = postService;
    }

    @Override
    public Page<PostResponse> cacheRetrievePostResponsePage(Long productId,
                                                            Pageable pageable){
        if (pageable.getPageNumber() == 0){
            ObjectWrapDto<Page<PostResponse>> wrapDto = wrapDtoMap.get(productId);
            if (wrapDto == null){
                Page<PostResponse> response = postService.retrieveProductQNA(productId, pageable);
                wrapDto = new ObjectWrapDto<>();
                wrapDto.setData(response);
                wrapDto.setKey(productId);
            }
            tempList.add(wrapDto);
            return wrapDto.getData();
        }else {
            return postService.retrieveProductQNA(productId, pageable);
        }
    }

}
