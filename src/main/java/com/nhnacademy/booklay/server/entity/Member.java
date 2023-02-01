package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "member")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @Column(name = "member_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_no")
    private Gender gender;

    @Column(name = "member_id")
    private String memberId;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column
    private String name;

    @Column
    private LocalDate birthday;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column
    private String email;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_blocked")
    @Setter
    private Boolean isBlocked;

    @Builder
    public Member(Gender gender, String memberId, String password, String nickname, String name,
                  LocalDate birthday, String phoneNo, String email, Boolean isBlocked) {
        this.gender = gender;
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthday = birthday;
        this.phoneNo = phoneNo;
        this.email = email;
        this.isBlocked = isBlocked;
    }

    public void updateMember(MemberUpdateRequest request, Gender gender) {
        this.gender = gender;
        this.password = request.getPassword();
        this.nickname = request.getNickname();
        this.name = request.getName();
        this.birthday = request.getBirthday();
        this.phoneNo = request.getPhoneNo();
        this.email = request.getEmail();
    }

    public void deleteMember() {
        this.deletedAt = LocalDateTime.now();
        this.password = "deleted";
        this.email = "deleted";
        this.name = "deleted";
        this.nickname = "deleted";
        this.phoneNo = "deleted";
    }

    public MemberGrade addGrade(String gradeName) {
        return MemberGrade.builder()
                          .member(this)
                          .name(gradeName)
                          .build();
    }
}
