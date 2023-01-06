package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "member")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @Column(name = "member_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_no")
    private Gender gender;

    @Column
    private String id;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Builder
    public Member(Gender gender, String id, String password, String nickname, String name, LocalDate birthday, String phoneNo, String email, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, Boolean isBlocked) {
        this.gender = gender;
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
}
