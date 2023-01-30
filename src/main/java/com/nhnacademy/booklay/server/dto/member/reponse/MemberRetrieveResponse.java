package com.nhnacademy.booklay.server.dto.member.reponse;

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
}
