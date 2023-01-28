package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import lombok.Setter;

@Table(name = "subscribe")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribe {

    @Id
    @Column(name = "subscribe_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @Setter
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
    public Subscribe(Product product, int subscribeWeek, int subscribeDay) {
        this.product = product;
        this.subscribeWeek = subscribeWeek;
        this.subscribeDay = subscribeDay;
    }
}
