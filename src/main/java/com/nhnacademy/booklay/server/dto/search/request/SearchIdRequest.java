package com.nhnacademy.booklay.server.dto.search.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchIdRequest {

    @NotNull
    private String classification;

    @NotNull
    private Long id;

}
