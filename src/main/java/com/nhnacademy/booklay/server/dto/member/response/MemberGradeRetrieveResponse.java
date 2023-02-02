package com.nhnacademy.booklay.server.dto.member.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberGradeRetrieveResponse {
    private Long id;
    private Long memberNo;
    private String name;
    private LocalDate date;
}
