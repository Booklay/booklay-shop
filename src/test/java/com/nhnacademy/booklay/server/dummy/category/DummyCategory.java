package com.nhnacademy.booklay.server.dummy.category;

import com.nhnacademy.booklay.server.entity.Category;

public class DummyCategory {

    public static Category getDummyCategory(){

        Category allProduct = Category.builder()
                .id(1L)
                .parent(null)
                .name("전체 상품")
                .depth(0)
                .isExposure(true)
                .build();

        return Category.builder()
                .id(101L)
                .parent(allProduct)
                .name("국내도서")
                .depth(2)
                .isExposure(true)
                .build();
    }
}
