package com.nhnacademy.booklay.server.dto.product.author.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class CreateAuthorRequest {

    String name;

    @Setter
    Long memberNo;

    public CreateAuthorRequest(String name) {
        this.name = name;
    }
}
