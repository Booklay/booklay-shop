package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.cache.ProductResponseWrapDto;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.PRODUCT_RESPONSE_KEY_NAME;

@Service
@RequiredArgsConstructor
public class ProductResponseCacheWrapServiceImpl implements ProductResponseCacheWrapService {
    private final RedisCacheService redisCacheService;
    private final ProductService productService;
    private ProductResponseWrapDto first;
    private ProductResponseWrapDto last;
    private final Map<Long, ProductResponseWrapDto> productResponseWrapDtoMap = new HashMap<>();
    //맵에 넣기 위한 대기열
    private final List<RetrieveProductResponse> tempList = Collections.synchronizedList(new LinkedList<>());
    private static final int MAX_CACHE_NUM = 200;


    @Override
    public List<RetrieveProductResponse> cacheRetrieveProductResponseList(List<Long> productIdList)
        throws IOException {
        List<Long> listForFind = new ArrayList<>();
        List<Integer> pointCheck = new ArrayList<>();
        List<RetrieveProductResponse> response = new ArrayList<>();
        for (int i = 0; i<productIdList.size(); i++){
            ProductResponseWrapDto wrapDto = productResponseWrapDtoMap.get(productIdList.get(i));
            if (wrapDto == null){
                pointCheck.add(i);
                listForFind.add(productIdList.get(i));
                response.add(null);
            }else {
                response.add(wrapDto.getData());
            }
        }
        List<RetrieveProductResponse> searchList = productService.retrieveProductResponses(listForFind);
        for (int i = 0; i<searchList.size(); i++){
            response.set(pointCheck.get(i),searchList.get(i));
        }
        tempList.addAll(searchList);
        return response;
    }




    @Override
    @Scheduled(fixedRate = 100)
    public void updateCheck(){
        List<Long> mapDeleteList = redisCacheService.updateCheck(PRODUCT_RESPONSE_KEY_NAME, productResponseWrapDtoMap);
        for (Long productNo:mapDeleteList){
            deleteFromMap(productNo);
        }

    }

    private void deleteFromMap(Long productId){
        ProductResponseWrapDto wrapDto = productResponseWrapDtoMap.get(productId);
        productResponseWrapDtoMap.remove(productId);
        wrapDto.getPrevious().setNext(wrapDto.getNext());
        wrapDto.getNext().setPrevious(wrapDto.getPrevious());
        if (wrapDto==last){
            last = null;
        }
        if (wrapDto==first){
            first = null;
        }
    }

    @Scheduled(cron = "0/1 * * * * *")
    private void addProductAllInOneInMap(){
        if (!tempList.isEmpty()){
            synchronized (tempList){
                while (!tempList.isEmpty()){
                    RetrieveProductResponse productAllInOneResponse = tempList.get(0);
                    tempList.remove(0);
                    setWrapDtoToLast(productAllInOneResponse);
                }
                while(productResponseWrapDtoMap.size() > MAX_CACHE_NUM){
                    RetrieveProductResponse removeTarget = first.getData();
                    productResponseWrapDtoMap.remove(removeTarget.getProductId());
                    first.getNext().setPrevious(null);
                    first = first.getNext();
                }
            }
        }
    }

    private void setWrapDtoToLast(RetrieveProductResponse productAllInOneResponse) {
        ProductResponseWrapDto wrapDto;
        if (productResponseWrapDtoMap.containsKey(productAllInOneResponse.getProductId())){
            wrapDto = productResponseWrapDtoMap.get(productAllInOneResponse.getProductId());
            setExistDtoToLast(wrapDto);
        }else {
            wrapDto = new ProductResponseWrapDto();
            wrapDto.setData(productAllInOneResponse);
            productResponseWrapDtoMap.put(wrapDto.getData().getProductId(),wrapDto);
        }
        if (wrapDto != last){
            wrapDto.setPrevious(last);
            last.setNext(wrapDto);
            last = wrapDto;
        }
        if(first == null){
            first = wrapDto;
        }
    }

    private void setExistDtoToLast(ProductResponseWrapDto wrapDto) {
        if (wrapDto.getNext() != null){
            if(wrapDto.getPrevious() != null){
                wrapDto.getPrevious().setNext(wrapDto.getNext());
            }
            wrapDto.getNext().setPrevious(wrapDto.getPrevious());
        }
    }

}
