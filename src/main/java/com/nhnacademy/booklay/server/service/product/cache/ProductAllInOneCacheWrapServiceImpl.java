package com.nhnacademy.booklay.server.service.product.cache;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.PRODUCT_ALL_IN_ONE_KEY_NAME;

import com.nhnacademy.booklay.server.dto.product.cache.ObjectWrapDto;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.utils.CacheServiceExtend;
import org.springframework.stereotype.Service;

@Service
public class ProductAllInOneCacheWrapServiceImpl extends CacheServiceExtend<ProductAllInOneResponse> implements ProductAllInOneCacheWrapService {


    private final ProductService productService;

    public ProductAllInOneCacheWrapServiceImpl(RedisCacheService redisCacheService,ProductService productService) {
        super(redisCacheService, PRODUCT_ALL_IN_ONE_KEY_NAME);
        this.productService = productService;
    }

    @Override
    public ProductAllInOneResponse cacheRetrieveProductAllInOne(Long productId){
        ObjectWrapDto<ProductAllInOneResponse> wrapDto = wrapDtoMap.get(productId);
        if (wrapDto == null){
            ProductAllInOneResponse response = productService.findProductById(productId);
            wrapDto = new ObjectWrapDto<>();
            wrapDto.setData(response);
            wrapDto.setKey(response.getInfo().getId());
            tempList.add(wrapDto);
        }
        return wrapDto.getData();
    }


}
