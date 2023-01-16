package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "product_tag")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductTag {

    @EmbeddedId
    private Pk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    @MapsId("productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_no")
    @MapsId("tagId")
    private Tag tag;

    @Embeddable
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pk implements Serializable {

        @Column(name = "product_no", nullable = false)
        private Long productId;

        @Column(name = "tag_no", nullable = false)
        private Long tagId;

    }

    @Builder
    public ProductTag(Pk pk, Product product, Tag tag) {
        this.pk = pk;
        this.product = product;
        this.tag = tag;
    }
}
