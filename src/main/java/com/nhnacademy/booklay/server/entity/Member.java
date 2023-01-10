package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private Long memberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_no")
    private Gender gender;

    @Column(name = "id")
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
    private Boolean isBlocked;

    @Builder
    public Member(Gender gender, String memberId, String password, String nickname, String name, LocalDate birthday, String phoneNo, String email, Boolean isBlocked) {
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

    public void update(Gender gender, String password, String nickname, String name, LocalDate birthday, String phoneNo, String email, Boolean isBlocked) {
        this.gender = gender;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthday = birthday;
        this.phoneNo = phoneNo;
        this.email = email;
        this.isBlocked = isBlocked;
    }
}
