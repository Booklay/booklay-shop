package com.nhnacademy.booklay.server.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Roles {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_AUTHOR("ROLE_AUTHOR"),
    ROLE_USER("ROLE_USER"),
    ROLE_ANONYMOUS("ROLE_ANONYMOUS");

    private final String value;
}
