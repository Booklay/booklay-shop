package com.nhnacademy.booklay.server.service.product.cache;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.PRODUCT_RECOMMEND_LIST;

import com.nhnacademy.booklay.server.dto.product.cache.ObjectWrapDto;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.utils.CacheServiceExtend;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class ProductRecommendIdListCacheWrapServiceImpl extends CacheServiceExtend<List<Long>>
    implements
    ProductRecommendIdListCacheWrapService {
    private final ProductRelationService productRelationService;
    private final ProductService productService;
    public ProductRecommendIdListCacheWrapServiceImpl(
        RedisCacheService redisCacheService, ProductRelationService productRelationService,
        ProductService productService) {
        super(redisCacheService, PRODUCT_RECOMMEND_LIST);
        this.productRelationService = productRelationService;
        this.productService = productService;
    }

    @Override
    public List<Long> cacheRetrieveRecommendProductIds(Long productId) {
        if (wrapDtoMap.containsKey(productId)){
            ObjectWrapDto<List<Long>> wrapDto = wrapDtoMap.get(productId);
            tempList.add(wrapDto);
            return wrapDto.getData();
        }else {
            List<Long> idList;
            if (productId == -1){
                idList = productService.retrieveLatestEightsIds();
            }else {
                idList =productRelationService.retrieveRecommendProductIds(productId);
            }
            ObjectWrapDto<List<Long>> wrapDto = new ObjectWrapDto<>();
            wrapDto.setData(idList);
            wrapDto.setKey(productId);
            tempList.add(wrapDto);
            return idList;
        }
    }

}
