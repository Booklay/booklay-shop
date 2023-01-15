package com.nhnacademy.booklay.server.dto.coupon;

import com.nhnacademy.booklay.server.entity.CouponTemplate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateCreateRequest {
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
    private Integer minimumUseAmount;
    private Integer maximumDiscountAmount;
    private Integer validateTerm;
    @NotNull
    private Boolean isDuplicatable;
    private Integer quantity;

    private String imagePath;

    public CouponTemplate toEntity(Long imageNo) {
        return CouponTemplate.builder()
            .name(name)
            .typeCode(typeCode)
            .amount(amount)
            .isOrderCoupon(isOrderCoupon)
            .applyItemId(applyItemId)
            .minimumUseAmount(minimumUseAmount)
            .maximumDiscountAmount(maximumDiscountAmount)
            .validateTerm(validateTerm)
            .isDuplicatable(isDuplicatable)
            .imageNo(imageNo)
            .build();
    }
}
