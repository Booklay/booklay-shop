package com.nhnacademy.booklay.server.dto.member.request;

import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.PointHistory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryCreateRequest {
    @NotNull
    private Long memberNo;

    @NotNull
    private Integer point;

    @NotBlank
    private String updatedDetail;

    public PointHistory toEntity(Member member, Integer currentTotalPoint) {
        return PointHistory.builder()
                           .member(member)
                           .point(this.point)
                           .totalPoint(currentTotalPoint + this.point)
                           .updatedDetail(this.updatedDetail)
                           .build();
    }
}
