package com.nhnacademy.booklay.server.dto.coupon;

import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Product;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponDetailRetrieveResponse {
    private final Long id;
    private final Member member;
    private final String name;
    private final String typeName;
    private final Long amount;
    private final Category category;
    private final Product product;
    private final Long minimumUseAmount;
    private final Long maximumDiscountAmount;
    private final LocalDateTime issuanceDeadlineAt;
    private final Boolean isDuplicatable;
}
