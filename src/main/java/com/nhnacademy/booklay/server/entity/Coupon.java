package com.nhnacademy.booklay.server.entity;

import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "coupon")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @Column(name = "coupon_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "image_no")
    private Image image;

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
    private Boolean isLimited;

    @Builder
    public Coupon(Image image, CouponType couponType, String name, int amount, int minimumUseAmount,
                  int maximumDiscountAmount, LocalDateTime issuanceDeadlineAt,
                  Boolean isDuplicatable, Boolean isLimited) {
        this.image = image;
        this.couponType = couponType;
        this.name = name;
        this.amount = amount;
        this.minimumUseAmount = minimumUseAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.issuanceDeadlineAt = issuanceDeadlineAt;
        this.isDuplicatable = isDuplicatable;
        this.isLimited = isLimited;
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
