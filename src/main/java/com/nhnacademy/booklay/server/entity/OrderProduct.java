package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "order_no", insertable = false, updatable = false)
    private Order order;
    @Column(name = "order_no")
    private Long orderNo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_no", insertable = false, updatable = false)
    private Product product;
    @Column(name = "product_no")
    private Long productNo;

    @Column
    private int count;

    @Column
    private int price;

    @Builder
    public OrderProduct(Order order, Long orderNo, Product product, Long productNo, int count,
                        int price) {
        this.order = order;
        this.orderNo = orderNo;
        this.product = product;
        this.productNo = productNo;
        this.count = count;
        this.price = price;
    }
}
