package com.nhnacademy.booklay.server.entity.document;

import lombok.Getter;

/**
 * 엘라스틱 상품 Docs 필드로 들어갈 태그 정보.
 */
@Getter
public class TagInfo {

    private final Long id;
    private final String name;


    public TagInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
