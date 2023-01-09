package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @Column(name = "coupon_no")
    private Long id;

    @OneToOne
    @JoinColumn(name = "image_no")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    private CouponType couponType;

    @Column
    private String name;

    @Column
    private int amount;

    @Column(name = "minimum_use_amount")
    private int minimumUseAmount;

    @Column(name = "maximum_discount_amount")
    private int maximumDiscountAmount;

    @Column(name = "issuance_deadline_at")
    private LocalDateTime issuanceDeadlineAt;

    @Column(name = "is_duplicatable")
    private Boolean isDuplicatable;

    @Builder
    public Coupon(Image image, CouponType couponType, String name, int amount, int minimumUseAmount,
                  int maximumDiscountAmount, LocalDateTime issuanceDeadlineAt,
                  Boolean isDuplicatable) {
        this.image = image;
        this.couponType = couponType;
        this.name = name;
        this.amount = amount;
        this.minimumUseAmount = minimumUseAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.issuanceDeadlineAt = issuanceDeadlineAt;
        this.isDuplicatable = isDuplicatable;
    }

}
