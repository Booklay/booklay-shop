package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Table(name = "order_status_code")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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
