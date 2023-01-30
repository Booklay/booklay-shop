package com.nhnacademy.booklay.server.dto.product.tag.response;

import lombok.Getter;

@Getter
public class TagProductResponse {

    private Long id;
    private String name;
    private boolean isRegistered;

    public TagProductResponse(Long id, String name, boolean isRegistered) {
        this.id = id;
        this.name = name;
        this.isRegistered = isRegistered;
    }
}