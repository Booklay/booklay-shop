package com.nhnacademy.booklay.server.dto.product.cache;

import lombok.Data;

@Data
public class ObjectWrapDto<T> {
    private Long key;
    private T data;
    private ObjectWrapDto<T> previous;
    private ObjectWrapDto<T> next;
}
