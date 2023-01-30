package com.nhnacademy.booklay.server.dto.member.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAuthorityUpdateRequest {
    @NotBlank
    private String authorityName;
}
