package com.nhnacademy.booklay.server.dto.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryDto {

    private final Long id;
    private final String name;
}
