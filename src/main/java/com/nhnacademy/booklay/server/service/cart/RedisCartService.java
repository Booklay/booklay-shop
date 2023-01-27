package com.nhnacademy.booklay.server.service.cart;

public interface RedisCartService extends MemberCartService {
    Integer checkUUID(String uuid);
}
