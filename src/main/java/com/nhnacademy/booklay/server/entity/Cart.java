package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "cart")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @EmbeddedId
    private Pk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", insertable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no", insertable = false, updatable = false)
    private Product product;

    @Column(name = "count")
    private Integer count;

    @Getter
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {

        @Column(name = "member_no")
        private Long memberId;

        @Column(name = "product_no")
        private Long productId;
    }

    @Builder
    public Cart(Pk pk, Member member, Product product, Integer count) {
        this.pk = pk;
        this.member = member;
        this.product = product;
        this.count = count;
    }
}
