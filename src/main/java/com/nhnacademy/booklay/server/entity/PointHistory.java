package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "point_history")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {

    @Id
    @Column(name = "point_history_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private Integer point;

    @Column(name = "total_point")
    private Integer totalPoint;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_detail")
    private String updatedDetail;

    @Builder
    public PointHistory(Long id, Member member, Integer point, Integer totalPoint, LocalDateTime updatedAt, String updatedDetail) {
        this.id = id;
        this.member = member;
        this.point = point;
        this.totalPoint = totalPoint;
        this.updatedAt = updatedAt;
        this.updatedDetail = updatedDetail;
    }
}
