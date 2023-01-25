package com.nhnacademy.booklay.server.dto.member.reponse;

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
    private String name;
    private LocalDate date;
}
