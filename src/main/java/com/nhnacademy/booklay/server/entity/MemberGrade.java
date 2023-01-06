package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "member_grade")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGrade {

    @Id
    @Column(name = "member_grade_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private String name;

    @Column
    private LocalDate date;

    @Builder
    public MemberGrade(Long id, Member member, String name, LocalDate date) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.date = date;
    }
}
