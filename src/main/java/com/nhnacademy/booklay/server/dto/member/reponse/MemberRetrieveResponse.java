package com.nhnacademy.booklay.server.dto.member.reponse;

import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberRetrieveResponse {
    private Long memberId;
    private String gender;
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

    /**
     * dto projection을 위한 생성자
     *
     * @author 양승아
     */
    public MemberRetrieveResponse(Long memberId, Gender gender, String id, String password,
                                  String nickname, String name, LocalDate birthday, String phoneNo,
                                  String email, LocalDateTime createdAt, LocalDateTime updatedAt,
                                  LocalDateTime deletedAt, Boolean isBlocked) {
        this.memberId = memberId;
        this.gender = gender.getName();
        this.id = id;
        this.password = password;
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

    public static MemberRetrieveResponse fromEntity(Member member) {
        return MemberRetrieveResponse.builder()
            .memberId(member.getMemberNo())
            .id(member.getMemberId())
            .password(member.getPassword())
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

    public static List<MemberRetrieveResponse> fromEntity(Page<Member> members) {
        return members.map(h->MemberRetrieveResponse.builder()
            .memberId(h.getMemberNo())
            .id(h.getMemberId())
            .password(h.getPassword())
            .name(h.getName())
            .nickname(h.getNickname())
            .birthday(h.getBirthday())
            .email(h.getEmail())
            .createdAt(h.getCreatedAt())
            .updatedAt(h.getUpdatedAt())
            .deletedAt(h.getDeletedAt())
            .gender(h.getGender().getName())
            .phoneNo(h.getPhoneNo())
            .isBlocked(h.getIsBlocked())
            .build())
            .getContent();
    }
}
