package com.nhnacademy.booklay.server.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class MemberInfoPostGetter {

    private Long member_info_memberNo;
    private String member_info_gender;
    private String member_info_memberId;
    private String member_info_nickname;
    private String member_info_name;
    private LocalDate member_info_birthday;
    private String member_info_phoneNo;
    private String member_info_email;

}
