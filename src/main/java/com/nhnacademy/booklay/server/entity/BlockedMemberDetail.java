package com.nhnacademy.booklay.server.entity;

import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private String reason;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Builder
    public BlockedMemberDetail(Long id, Member member, String reason, LocalDateTime blockedAt, LocalDateTime releasedAt) {
        this.id = id;
        this.member = member;
        this.reason = reason;
        this.blockedAt = blockedAt;
        this.releasedAt = releasedAt;
    }
}
