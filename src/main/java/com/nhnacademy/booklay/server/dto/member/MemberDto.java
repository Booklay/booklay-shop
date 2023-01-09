package com.nhnacademy.booklay.server.dto.member;

import com.nhnacademy.booklay.server.entity.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberDto {
    private Long memberId;
    private Gender gender;
    private String id;
    private String password;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private String phoneNo;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean isBlocked;
}
