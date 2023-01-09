package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "order_product")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id
    @Column(name = "order_product_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no")
    private Order order;

    @OneToOne
    @JoinColumn(name = "product_no")
    private Product product;

    @Column
    private int count;

    @Column
    private int price;

    @Builder
    public OrderProduct(Order order, Product product, int count, int price) {
        this.order = order;
        this.product = product;
        this.count = count;
        this.price = price;
    }
}
