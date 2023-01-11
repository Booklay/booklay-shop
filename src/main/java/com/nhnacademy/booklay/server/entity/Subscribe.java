package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "subscribe")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribe {

    @Id
    @Column(name = "subscribe_no")
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no", nullable = false)
    private Product product;

    @Column(name = "subscribe_week", nullable = false)
    private int subscribeWeek;

    @Column(name = "subscribe_day", nullable = false)
    private int subscribeDay;

    @Setter
    @Column(name = "publisher", length = 100)
    private String publisher;

    @Builder
    public Subscribe(Long id, Product product, int subscribeWeek, int subscribeDay) {
        this.id = id;
        this.product = product;
        this.subscribeWeek = subscribeWeek;
        this.subscribeDay = subscribeDay;
    }
}
