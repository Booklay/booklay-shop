package com.nhnacademy.booklay.server.dto.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotNull
    private Long typeCode;
    @NotNull
    private Integer amount;
    @NotNull
    private Boolean isOrderCoupon;
    @NotNull
    private Long applyItemId;
    @NotNull
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime issuanceDeadlineAt;
    @NotNull
    private Boolean isDuplicatable;
    private Integer quantity;

    public static Coupon toEntity(CouponCreateRequest couponRequest, CouponType couponType) {
        return Coupon.builder()
            .couponType(couponType)
            .name(couponRequest.getName())
            .amount(couponRequest.getAmount())
            .minimumUseAmount(couponRequest.getMinimumUseAmount())
            .maximumDiscountAmount(couponRequest.getMaximumDiscountAmount())
            .issuanceDeadlineAt(couponRequest.getIssuanceDeadlineAt())
            .isDuplicatable(couponRequest.getIsDuplicatable())
            .build();
    }
}
