package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "delivery_status_code")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryStatusCode {

    @Id
    @Column(name = "code")
    private Integer id;

    @Column(length = 10, nullable = false)
    private String name;

    @Builder
    public DeliveryStatusCode(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}