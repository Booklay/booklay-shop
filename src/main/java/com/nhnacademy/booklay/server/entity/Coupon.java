package com.nhnacademy.booklay.server.entity;

import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import java.time.LocalDateTime;
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
import lombok.Setter;

@Table(name = "coupon")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("java:S107")
public class Coupon {

    @Id
    @Column(name = "coupon_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "image_no")
    private ObjectFile objectFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    private CouponType couponType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    @Setter
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_no")
    @Setter
    private Category category;

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

    @Column(name = "is_limited")
    @Setter
    private Boolean isLimited;

    @Builder
    public Coupon(ObjectFile objectFile, CouponType couponType, String name, int amount,
                  int minimumUseAmount,
                  int maximumDiscountAmount, LocalDateTime issuanceDeadlineAt,
                  Boolean isDuplicatable) {
        this.objectFile = objectFile;
        this.couponType = couponType;
        this.name = name;
        this.amount = amount;
        this.minimumUseAmount = minimumUseAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.issuanceDeadlineAt = issuanceDeadlineAt;
        this.isDuplicatable = isDuplicatable;
    }

    public void update(CouponUpdateRequest couponUpdateRequest, CouponType couponType) {
        this.name = couponUpdateRequest.getName();
        this.couponType = couponType;
        this.amount = couponUpdateRequest.getAmount();
        this.minimumUseAmount = couponUpdateRequest.getMinimumUseAmount();
        this.maximumDiscountAmount = couponUpdateRequest.getMaximumDiscountAmount();
        this.issuanceDeadlineAt = couponUpdateRequest.getIssuanceDeadlineAt();
        this.isDuplicatable = couponUpdateRequest.getIsDuplicatable();
    }

}
