package com.nhnacademy.booklay.server.dto.member.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthorityUpdateRequest {
    @NotBlank
    private String authorityName;
}
