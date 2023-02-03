package com.nhnacademy.booklay.server.dto.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberGradeChartRetrieveResponse {
    private Long whiteCount;
    private Long silverCount;
    private Long goldCount;
    private Long PlatinumCount;
}
