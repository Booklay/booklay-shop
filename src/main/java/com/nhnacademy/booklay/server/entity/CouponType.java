package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

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
