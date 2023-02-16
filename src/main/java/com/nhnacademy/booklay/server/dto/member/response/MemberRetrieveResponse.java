package com.nhnacademy.booklay.server.dto.member.response;

import com.nhnacademy.booklay.server.constant.Roles;
import com.nhnacademy.booklay.server.entity.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRetrieveResponse {
    private Long memberNo;
    private String gender;
    private String memberId;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private String phoneNo;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean isBlocked;
    private String memberGrade;
    private String authority;

    public static MemberRetrieveResponse fromEntity(Member member) {
        return MemberRetrieveResponse.builder()
                                     .memberNo(member.getMemberNo())
                                     .memberId(member.getMemberId())
                                     .name(member.getName())
                                     .nickname(member.getNickname())
                                     .birthday(member.getBirthday())
                                     .email(member.getEmail())
                                     .createdAt(member.getCreatedAt())
                                     .updatedAt(member.getUpdatedAt())
                                     .deletedAt(member.getDeletedAt())
                                     .gender(member.getGender().getName())
                                     .phoneNo(member.getPhoneNo())
                                     .isBlocked(member.getIsBlocked())
                                     .build();
    }

    public MemberRetrieveResponse(Long memberNo, String gender, String memberId, String nickname,
                                  String name, LocalDate birthday, String phoneNo, String email,
                                  LocalDateTime createdAt, LocalDateTime updatedAt,
                                  LocalDateTime deletedAt, Boolean isBlocked) {
        this.memberNo = memberNo;
        this.gender = gender;
        this.memberId = memberId;
        this.nickname = nickname;
        this.name = name;
        this.birthday = birthday;
        this.phoneNo = phoneNo;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isBlocked = isBlocked;
    }
}
