package com.nhnacademy.booklay.server.service.category;

import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.repository.product.CategoryProductRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class CategoryProductServiceImpl implements CategoryProductService{
    private final CategoryProductRepository categoryProductRepository;

    @Override
    public List<Long> retrieveCategoryIdListByProductId(Long productId){
        List<Long> returnList = new ArrayList<>();
        for (CategoryProduct categoryProduct: categoryProductRepository.findAllByProduct_Id(productId)){
            returnList.add(categoryProduct.getPk().getCategoryId());
        }
        return returnList;
    }

    /**
     *
     * @param productIdList 상품 번호 리스트
     * @return 상품 번호를 키값으로 카테고리번호리스트를 반환해줌
     */
    @Override
    public MultiValueMap<Long, Long>retrieveCategoryIdListMultiValueMapByProductIdList(
        List<Long> productIdList){
        MultiValueMap<Long, Long> returnMap = new LinkedMultiValueMap<>();
        for (CategoryProduct categoryProduct: categoryProductRepository.findAllByProduct_IdIn(productIdList)){
            returnMap.add(categoryProduct.getPk().getProductId(), categoryProduct.getPk()
                .getCategoryId());
        }
        return returnMap;
    }
}
