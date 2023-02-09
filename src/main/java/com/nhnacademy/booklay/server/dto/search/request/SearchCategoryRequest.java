package com.nhnacademy.booklay.server.dto.search.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchCategoryRequest {

    @NotNull
    private Long categoryId;

}
