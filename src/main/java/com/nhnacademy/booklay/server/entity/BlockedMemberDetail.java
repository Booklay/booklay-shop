package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "blocked_member_detail")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Builder
    public BlockedMemberDetail(Member member, String reason) {
        this.member = member;
        this.reason = reason;
    }
}
