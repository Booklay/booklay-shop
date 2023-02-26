package com.nhnacademy.booklay.server.dto.product.cache;

import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import lombok.Data;

@Data
public class ProductAllInOneWrapDto {
    private ProductAllInOneResponse data;
    private ProductAllInOneWrapDto previous;
    private ProductAllInOneWrapDto next;
}
