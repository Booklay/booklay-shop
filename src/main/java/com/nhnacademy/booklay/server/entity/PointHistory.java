package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "point_history")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointHistory {

    @Id
    @Column(name = "point_history_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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
}
