package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "blocked_member_detail")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
