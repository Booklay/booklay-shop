package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "product_relation")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRelation {

    @Id
    @Column(name = "product_relation_no")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_product_no", nullable = false)
    private Product baseProduct;

    // TODO 넘버링 제거 필요
    @ManyToOne
    @JoinColumn(name = "related_product_no2", nullable = false)
    private Product relatedProduct;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Builder
    public ProductRelation(Long id, Product baseProduct, Product relatedProduct,
                           Boolean isDeleted) {
        this.id = id;
        this.baseProduct = baseProduct;
        this.relatedProduct = relatedProduct;
        this.isDeleted = isDeleted;
    }
}
