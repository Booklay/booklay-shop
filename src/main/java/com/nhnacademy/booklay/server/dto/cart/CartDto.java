package com.nhnacademy.booklay.server.dto.cart;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    @NotNull
    private Long productNo;
    @NotNull
    private Integer count;
}
