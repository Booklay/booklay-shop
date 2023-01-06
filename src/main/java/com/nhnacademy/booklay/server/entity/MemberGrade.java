package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "member_grade")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberGrade {

    @Id
    @Column(name = "member_grade_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private String name;

    @Column
    private LocalDate date;
}
