package com.nhnacademy.booklay.server.service.cart;

public interface RedisCartService extends CartService{
    Integer checkUUID(String uuid);
}
