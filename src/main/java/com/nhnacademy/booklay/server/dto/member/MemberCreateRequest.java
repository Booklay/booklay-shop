package com.nhnacademy.booklay.server.dto.member;

import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCreateRequest {
    @NotBlank
    private String gender;
    @NotBlank
    private String id;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
    @NotBlank
    private String name;
    @NotBlank
    private LocalDate birthday;
    @NotBlank
    private String phoneNo;
    @Email
    @NotBlank
    private String email;

    public Member toEntity(Gender gender) {
        return Member.builder()
            .id(this.id)
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
