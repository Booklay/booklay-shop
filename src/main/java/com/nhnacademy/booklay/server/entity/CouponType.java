package com.nhnacademy.booklay.server.entity;

import javax.persistence.EntityListeners;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "coupon_type")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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
