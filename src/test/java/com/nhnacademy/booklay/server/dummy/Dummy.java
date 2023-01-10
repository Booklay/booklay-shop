package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.entity.DeliveryStatusCode;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.entity.OrderProduct;
import com.nhnacademy.booklay.server.entity.OrderStatusCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class Dummy {
    public static Gender getDummyGender() {
        return Gender.builder()
            .id(1L)
            .name("M")
            .build();
    }

    public static Member getDummyMember() {
        Member member = Member.builder()
            .gender(getDummyGender())
            .memberId("dummyMemberId")
            .password("$2a$12$5KoVJnK1WF2h4h4T3FmifeO3ZLtAjiayJ783EfvTs7zSIz2GUhnMu") //1234
            .nickname("메뚜기")
            .name("유재석")
            .birthday(LocalDate.now())
            .phoneNo("01012341234")
            .email("abcd@naver.com")
            .isBlocked(false)
            .build();

        ReflectionTestUtils.setField(member, "memberNo", 1L);

        return member;
    }

    public static Coupon getDummyCoupon() {

        CouponType couponType = CouponType.builder()
            .name("정액")
            .build();

        ReflectionTestUtils.setField(couponType, "id", 1L);

        Coupon coupon = Coupon.builder()
            .image(DummyCart.getDummyImage())
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
            .name("admin")
            .build();
    }

    public static MemberAuthority getDummyMemberAuthority() {
        Member member = getDummyMember();
        Authority authority = getDummyAuthority();

        return MemberAuthority.builder()
            .pk(new MemberAuthority.Pk(member.getMemberNo(), authority.getId()))
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

    public static DeliveryDetail getDummyDeliveryDetail() {

        DeliveryDetail deliveryDetail = DeliveryDetail.builder()
            .order(getDummyOrder())
            .statusCode(getDummyDeliveryStatusCode())
            .zipCode("11111")
            .address("우리집 바둑이네 밥그릇")
            .sender("Dumb")
            .senderPhoneNumber("010-1234-5678")
            .receiver("Dumber")
            .receiverPhoneNumber("010-9876-5432")
            .build();

        deliveryDetail.setCompletedAt(LocalDateTime.now());

        ReflectionTestUtils.setField(deliveryDetail, "id", 1L);
        ReflectionTestUtils.setField(deliveryDetail, "deliveryStartAt", LocalDateTime.now());

        return deliveryDetail;
    }

    public static DeliveryStatusCode getDummyDeliveryStatusCode() {
        return DeliveryStatusCode.builder()
            .id(1)
            .name("배송중")
            .build();
    }

    public static Order getDummyOrder() {
        Order order = Order.builder()
            .member(Dummy.getDummyMember())
            .orderStatusCode(getDummyOrderStatusCode())
            .productPriceSum(30000L)
            .deliveryPrice(1000L)
            .discountPrice(0L)
            .pointUsePrice(2000L)
            .paymentPrice(31000L)
            .paymentMethod(3L)
            .giftWrappingPrice(4500L)
            .isBlinded(false)
            .build();

        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "orderedAt", LocalDateTime.now());

        return order;
    }

    public static OrderStatusCode getDummyOrderStatusCode() {
        return OrderStatusCode.builder()
            .id(1L)
            .name("입금대기중")
            .build();
    }
}
