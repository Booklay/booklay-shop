package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.*;
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
            .nickname("메뚜기")
            .name("유재석")
            .birthday(LocalDate.now())
            .phoneNo("01012341234")
            .email("www.abcd.com")
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

    public static OrderProduct getDummyOrderProduct() {
        OrderProduct orderProduct = OrderProduct.builder()
            .order(null)
            .product(null)
            .count(1)
            .price(10000)
            .build();

        ReflectionTestUtils.setField(orderProduct, "id", 1L);

        return orderProduct;

    }

    public static Authority getDummyAuthority() {

        return Authority.builder()
            .id(1L)
            .authority("admin")
            .build();
    }

    public static MemberAuthority getDummyMemberAuthority() {
        Member member = getDummyMember();
        Authority authority = getDummyAuthority();

        return MemberAuthority.builder()
            .pk(new MemberAuthority.Pk(member.getMemberId(), authority.getId()))
            .member(member)
            .authority(authority)
            .build();
    }

    public static MemberGrade getDummyMemberGrade() {
        MemberGrade memberGrade = MemberGrade.builder()
            .member(getDummyMember())
            .name("white")
            .build();

        ReflectionTestUtils.setField(memberGrade, "id", 1L);

        return memberGrade;
    }

    public static Category getDummyCategory() {

        Category allProduct = Category.builder()
            .id(1L)
            .parent(null)
            .name("전체 상품")
            .depth(0L)
            .isExposure(true)
            .build();

        return Category.builder()
            .id(101L)
            .parent(allProduct)
            .name("국내도서")
            .depth(allProduct.getDepth() + 1)
            .isExposure(true)
            .build();
    }
}
