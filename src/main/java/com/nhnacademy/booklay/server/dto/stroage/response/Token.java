package com.nhnacademy.booklay.server.dto.stroage.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {

    private String id;

    private String expires;

    private Tenant tenant;

}
