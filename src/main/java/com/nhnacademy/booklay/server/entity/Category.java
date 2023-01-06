package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @Column(name = "category_no")
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

    @Builder
    public Category(Long id, Category parent, String name, Integer depth, Boolean isExposure) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.depth = depth;
        this.isExposure = isExposure;
    }
}
