package com.nhnacademy.booklay.server.dto.order;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import java.util.List;
import lombok.Getter;

@Getter
public class StorageRequest {
    private List<CartDto> cartDtoList;

}