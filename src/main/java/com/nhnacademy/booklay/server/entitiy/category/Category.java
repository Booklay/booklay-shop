package com.nhnacademy.booklay.server.entitiy.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * javadoc. 카테고리 entity
 */
@Table(name = "category")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @Column(name = "category_no")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_category_no")
    private Category parentCategory;

    private String name;

    private int depth;

    @Column(name = "is_exposure")
    private boolean isExposure;
}
