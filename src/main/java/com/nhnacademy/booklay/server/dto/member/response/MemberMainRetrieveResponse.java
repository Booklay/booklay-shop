package com.nhnacademy.booklay.server.dto.member.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberMainRetrieveResponse {
    private Long memberNo;
    private String gender;
    private String memberId;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private String phoneNo;
    private String email;
    private String memberGrade;
    private Integer currentTotalPoint;

    public void maskingMember() {
        this.memberId = this.memberId.replaceAll("(?<=.{3}).", "*");
        this.name = this.name.replaceAll("(?<=.{2}).", "*");
        this.phoneNo = this.phoneNo.replaceAll("(?<=.{7}).", "*");
    }
}
