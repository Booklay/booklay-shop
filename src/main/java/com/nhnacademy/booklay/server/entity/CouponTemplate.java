package com.nhnacademy.booklay.server.entity;

import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "coupon_template")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponTemplate {

    @Id
    @Column(name = "coupon_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_no")
    private Long imageNo;

    @Column(name = "code")
    private Long typeCode;

    @Column(name = "is_order_coupon")
    private Boolean isOrderCoupon;
    @Column(name = "apply_item_id")
    private Long applyItemId;

    @Column
    private String name;

    @Column
    private int amount;

    @Column(name = "minimum_use_amount")
    private int minimumUseAmount;

    @Column(name = "maximum_discount_amount")
    private int maximumDiscountAmount;

    @Column(name = "validate_term")
    private Integer validateTerm;

    @Column(name = "is_duplicatable")
    private Boolean isDuplicatable;


    @Builder
    public CouponTemplate(Long id, Long imageNo, Long typeCode, Boolean isOrderCoupon,
                          Long applyItemId,
                          String name, int amount, int minimumUseAmount, int maximumDiscountAmount,
                          Integer validateTerm, Boolean isDuplicatable) {
        this.id = id;
        this.imageNo = imageNo;
        this.typeCode = typeCode;
        this.isOrderCoupon = isOrderCoupon;
        this.applyItemId = applyItemId;
        this.name = name;
        this.amount = amount;
        this.minimumUseAmount = minimumUseAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.validateTerm = validateTerm;
        this.isDuplicatable = isDuplicatable;
    }

    public CouponCreateRequest toCouponCreateRequest() {
        return new CouponCreateRequest(name, typeCode, amount
            , isOrderCoupon, applyItemId, minimumUseAmount, maximumDiscountAmount,
            LocalDateTime.now().plusDays(validateTerm),isDuplicatable, 1);
    }
}
