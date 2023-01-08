package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @Column(name = "category_no")
    private Long id;

    // TODO NotNull -> nullable = true ERD와 SQL 변경 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_no", nullable = true)
    private Category parent;

    @Column
    private String name;

    @Column
    private Long depth;

    @Column(name = "is_exposure")
    private Boolean isExposure;

    @Builder
    public Category(Long id, Category parent, String name, Long depth, Boolean isExposure) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.depth = depth;
        this.isExposure = isExposure;
    }
}
