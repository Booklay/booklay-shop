package com.nhnacademy.booklay.server.dto.product.tag.response;

import lombok.Getter;

@Getter
public class RetrieveTagResponse {
    private Long id;
    private String name;

    public RetrieveTagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
