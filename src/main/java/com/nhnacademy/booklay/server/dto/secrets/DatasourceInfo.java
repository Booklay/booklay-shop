package com.nhnacademy.booklay.server.dto.secrets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DatasourceInfo {

    private final String username;

    private final String passwword;

    private final String dbUrl;

}
