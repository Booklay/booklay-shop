package com.nhnacademy.booklay.server.dto.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponCURequest {
    @NotBlank
    private String name;
    private Long memberId;
    @NotNull
    private Long typeCode;
    @NotNull
    private Integer amount;
    private long categoryId;
    private long productId;
    @NotNull
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issuanceDeadlineAt;
    @NotNull
    private Boolean isDuplicatable;
}
