package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.entity.Member;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Dummy {

    public static Member getDummyMember() {
        Gender gender = Gender.builder()
                .id(1L)
                .gender("M")
                .build();

        Member member = Member.builder()
                .gender(gender)
                .id("dummyMemberId")
                .password("$2a$12$5KoVJnK1WF2h4h4T3FmifeO3ZLtAjiayJ783EfvTs7zSIz2GUhnMu") //1234
                .nickname("유재석")
                .name("강호동")
                .birthday(LocalDate.now())
                .phoneNo("01012341234")
                .email("www.abcd.com")
                .createdAt(LocalDateTime.now())
                .isBlocked(false)
                .build();

        ReflectionTestUtils.setField(member, "memberId", 1L);

        return member;
    }

    public static Coupon getDummyCoupon() {
        Image image = Image.builder()
            .ext("")
            .address("")
            .build();

        ReflectionTestUtils.setField(image, "id", 1L);

        CouponType couponType = CouponType.builder()
            .name("정액")
            .build();

        ReflectionTestUtils.setField(couponType, "id", 1L);

        Coupon coupon = Coupon.builder()
            .image(image)
            .couponType(couponType)
            .name("이달의 쿠폰")
            .amount(5)
            .minimumUseAmount(1000)
            .maximumDiscountAmount(3000)
            .issuanceDeadlineAt(LocalDateTime.of(2023, 1, 20, 0, 0, 0))
            .isDuplicatable(false)
            .build();

        ReflectionTestUtils.setField(coupon, "id", 1L);

        return coupon;
    }
}
