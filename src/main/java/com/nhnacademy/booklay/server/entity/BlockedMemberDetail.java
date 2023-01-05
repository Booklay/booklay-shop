package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "blocked_member_detail")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedMemberDetail {

    @Id
    @Column(name = "blocked_member_detail_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private String reason;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;
}
