package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "coupon_type")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponType {

    @Id
    @Column(name = "code")
    private Long id;

    @Column
    private String name;

    @Builder
    public CouponType(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
