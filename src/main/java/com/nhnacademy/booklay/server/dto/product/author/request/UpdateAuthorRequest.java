package com.nhnacademy.booklay.server.dto.product.author.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UpdateAuthorRequest {

    @NotNull
    Long id;

    @NotNull
    String name;

    @Setter
    Long memberNo;

    public UpdateAuthorRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
