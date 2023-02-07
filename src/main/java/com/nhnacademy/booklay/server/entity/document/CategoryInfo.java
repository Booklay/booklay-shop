package com.nhnacademy.booklay.server.entity.document;

import lombok.Getter;

/**
 * 엘라스틱 상품 Docs 필드로 들어갈 카테고리 정보.
 */
@Getter
public class CategoryInfo {
    Long id;
    String name;

    public CategoryInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
