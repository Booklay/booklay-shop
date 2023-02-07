package com.nhnacademy.booklay.server.entity.document;

/**
 * 엘라스틱 상품 Docs 필드로 들어갈 작가 정보.
 */
public class AuthorInfo {

    Long id;
    String name;

    public AuthorInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
