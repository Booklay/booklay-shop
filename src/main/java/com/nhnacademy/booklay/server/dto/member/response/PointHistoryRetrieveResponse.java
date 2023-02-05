package com.nhnacademy.booklay.server.dto.member.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryRetrieveResponse {
    private Long id;
    private Long member;
    private Integer point;
    private Integer totalPoint;
    private LocalDateTime updatedAt;
    private String updatedDetail;
}
