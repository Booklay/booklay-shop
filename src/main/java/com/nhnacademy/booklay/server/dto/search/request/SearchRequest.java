package com.nhnacademy.booklay.server.dto.search.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchRequest {

    @NotBlank
    String keywords;
}