package com.nhnacademy.booklay.server.dto.order.payment;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StorageRequest {
    private List<CartDto> cartDtoList;
}
