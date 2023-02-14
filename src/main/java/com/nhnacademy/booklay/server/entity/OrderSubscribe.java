package com.nhnacademy.booklay.server.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;



@Table(name = "order_subscribe")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class OrderSubscribe {


    @Id
    @Column(name = "order_subscribe_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribe_no", insertable = false, updatable = false)
    private Subscribe subscribe;
    @Column(name = "subscribe_no")
    private Long subscribeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no", insertable = false, updatable = false)
    private Order order;
    @Column(name = "order_no")
    private Long orderNo;

    @Column
    private Integer amounts;

    @Column Long price;

    @Column(name = "start_at")
    private LocalDate startAt;
    @Column(name = "finish_at")
    private LocalDate finishAt;

    @Builder
    public OrderSubscribe(Long id, Subscribe subscribe, Long subscribeNo, Order order, Long orderNo,
                          Integer amounts, Long price, LocalDate startAt, LocalDate finishAt) {
        this.id = id;
        this.subscribe = subscribe;
        this.subscribeNo = subscribeNo;
        this.order = order;
        this.orderNo = orderNo;
        this.amounts = amounts;
        this.price = price;
        this.startAt = startAt;
        this.finishAt = finishAt;
    }

}