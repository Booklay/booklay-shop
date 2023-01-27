package com.nhnacademy.booklay.server.dto.member.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointPresentRequest {
    private String targetMemberId;
    private Integer targetPoint;
}
