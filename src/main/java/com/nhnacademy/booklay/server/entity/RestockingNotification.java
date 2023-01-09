package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestockingNotification {


    @EmbeddedId
    private Pk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    @MapsId("memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    @MapsId("productId")
    private Product product;

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
    public RestockingNotification( Member member, Product product) {
        this.member = member;
        this.product = product;
    }
}
