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
public class OwnedEbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owned_ebook_no")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    Member member;


    @Builder
    public OwnedEbook(Product product, Member member) {
        this.product = product;
        this.member = member;
    }
}
