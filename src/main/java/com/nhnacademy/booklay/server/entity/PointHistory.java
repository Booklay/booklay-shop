package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "point_history")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_detail")
    private String updatedDetail;

    @Builder
    public PointHistory(Member member, Integer point, Integer totalPoint, String updatedDetail) {
        this.member = member;
        this.point = point;
        this.totalPoint = totalPoint;
        this.updatedDetail = updatedDetail;
    }
}
