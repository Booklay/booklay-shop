package com.nhnacademy.booklay.server.dto.stroage.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordCredentials {

    private String username;
    private String password;

}
