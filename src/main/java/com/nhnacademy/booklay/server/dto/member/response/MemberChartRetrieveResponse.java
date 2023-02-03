package com.nhnacademy.booklay.server.dto.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberChartRetrieveResponse {
    private Long validMemberCount;
    private Long blockedMemberCount;
    private Long droppedMemberCount;
}
