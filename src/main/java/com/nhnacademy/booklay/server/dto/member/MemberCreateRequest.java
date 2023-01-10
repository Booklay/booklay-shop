package com.nhnacademy.booklay.server.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCreateRequest {
    @NotBlank
    private String gender;
    @NotBlank
    private String memberId;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
    @NotBlank
    private String name;
    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @NotBlank
    private String phoneNo;
    @Email
    @NotBlank
    private String email;

    public Member toEntity(Gender gender) {
        return Member.builder()
            .memberId(this.memberId)
            .password(this.password)
            .gender(gender)
            .nickname(this.nickname)
            .name(this.name)
            .birthday(this.birthday)
            .phoneNo(this.phoneNo)
            .email(this.email)
            .isBlocked(false)
            .build();
    }
}
