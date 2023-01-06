package com.nhnacademy.booklay.server.entity;

import lombok.*;
import org.springframework.stereotype.Controller;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "order_status_code")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatusCode {

    @Id
    @Column(name = "code")
    private Long id;

    @Column
    private String name;

    @Builder
    public OrderStatusCode(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
