package com.nhnacademy.booklay.server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryProduct {

    @EmbeddedId
    private Pk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_no")
    @MapsId("categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    @MapsId("productId")
    private Product product;


    @Getter
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {
        @Column(name = "category_no")
        private Long categoryId;

        @Column(name = "product_no")
        private Long productId;
    }

    @Builder
    public CategoryProduct(Pk pk, Category category, Product product) {
        this.pk = pk;
        this.category = category;
        this.product = product;
    }
}
