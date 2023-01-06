package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;

@Table
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {

    @Id
    @Column(name = "category_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_no")
    private Category parent;

    @Column
    private String name;

    @Column
    private Integer depth;

    @Column(name = "is_exposure")
    private Boolean isExposure;
}
