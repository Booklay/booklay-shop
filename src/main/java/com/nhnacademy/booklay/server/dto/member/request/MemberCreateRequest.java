package com.nhnacademy.booklay.server.dto.member.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
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
