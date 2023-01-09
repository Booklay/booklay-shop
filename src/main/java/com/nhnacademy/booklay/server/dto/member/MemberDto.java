package com.nhnacademy.booklay.server.dto.member;

import com.nhnacademy.booklay.server.entity.Gender;
import java.time.LocalDate;

public class MemberDto {
    private Gender gender;
    private String id;
    private String password;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private String phoneNo;
    private String email;
    private Boolean isBlocked;
}
