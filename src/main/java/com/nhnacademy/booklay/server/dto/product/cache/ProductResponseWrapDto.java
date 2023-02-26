package com.nhnacademy.booklay.server.dto.product.cache;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import lombok.Data;

@Data
public class ProductResponseWrapDto {
    private RetrieveProductResponse data;
    private ProductResponseWrapDto previous;
    private ProductResponseWrapDto next;
}
