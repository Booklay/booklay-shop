package com.nhnacademy.booklay.server.service.product.cache;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.PRODUCT_RESPONSE_KEY_NAME;

import com.nhnacademy.booklay.server.dto.product.cache.ObjectWrapDto;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.utils.CacheServiceExtend;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductResponseCacheWrapServiceImpl extends CacheServiceExtend<RetrieveProductResponse> implements ProductResponseCacheWrapService {
    private final ProductService productService;
    public ProductResponseCacheWrapServiceImpl(RedisCacheService redisCacheService, ProductService productService) {
        super(redisCacheService, PRODUCT_RESPONSE_KEY_NAME);
        this.productService = productService;
    }

    @Override
    public List<RetrieveProductResponse> cacheRetrieveProductResponseList(List<Long> productIdList)
        throws IOException {
        List<Long> listForFind = new ArrayList<>();
        List<Integer> pointCheck = new ArrayList<>();
        List<RetrieveProductResponse> response = new ArrayList<>();
        List<ObjectWrapDto<RetrieveProductResponse>> retrieveDtoList = new ArrayList<>();
        for (int i = 0; i<productIdList.size(); i++){
            ObjectWrapDto<RetrieveProductResponse> wrapDto = wrapDtoMap.get(productIdList.get(i));
            if (wrapDto == null){
                pointCheck.add(i);
                listForFind.add(productIdList.get(i));
                response.add(null);
            }else {
                retrieveDtoList.add(wrapDto);
                response.add(wrapDto.getData());
            }
        }
        if (!listForFind.isEmpty()){
            List<RetrieveProductResponse> searchList = productService.retrieveProductResponses(listForFind);
            for (int i = 0; i<searchList.size(); i++){
                ObjectWrapDto<RetrieveProductResponse> wrapDto = new ObjectWrapDto<>();
                wrapDto.setKey(searchList.get(i).getProductId());
                wrapDto.setData(searchList.get(i));
                retrieveDtoList.add(wrapDto);
                response.set(pointCheck.get(i),searchList.get(i));
            }
        }
        tempList.addAll(retrieveDtoList);
        return response;
    }

}
