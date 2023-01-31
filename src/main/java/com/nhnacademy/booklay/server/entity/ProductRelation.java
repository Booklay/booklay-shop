package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_product_no", nullable = false)
    private Product baseProduct;

    @ManyToOne
    @JoinColumn(name = "related_product_no", nullable = false)
    private Product relatedProduct;

    @Builder
    public ProductRelation(Product baseProduct, Product relatedProduct) {
        this.baseProduct = baseProduct;
        this.relatedProduct = relatedProduct;
    }
}
