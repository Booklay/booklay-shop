package com.nhnacademy.booklay.server.dto.stroage.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Auth {

    private String tenantId;
    private PasswordCredentials passwordCredentials;

}
