package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.BlockedMemberDetail;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.entity.DeliveryStatusCode;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.entity.OrderCoupon;
import com.nhnacademy.booklay.server.entity.OrderProduct;
import com.nhnacademy.booklay.server.entity.OrderStatusCode;
import com.nhnacademy.booklay.server.entity.PointHistory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
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
        ReflectionTestUtils.setField(member, "createdAt", LocalDateTime.now());

        return member;
    }

    public static DeliveryDestination getDummyDeliveryDestination() {
        DeliveryDestination deliveryDestination = DeliveryDestination.builder()
            .member(getDummyMember())
            .name("집")
            .zipCode("61452")
            .address("광주광역시 동구 필문대로 309")
            .isDefaultDestination(true)
            .build();
        return deliveryDestination;
    }

    public static Coupon getDummyCoupon() {
        Coupon coupon = Coupon.builder()
            .objectFile(DummyCart.getDummyFile())
            .couponType(Dummy.getDummyCouponType())
            .name("이달의 쿠폰")
            .amount(5)
            .minimumUseAmount(1000)
            .maximumDiscountAmount(3000)
            .issuanceDeadlineAt(LocalDateTime.of(2023, 1, 20, 0, 0, 0))
            .isDuplicatable(false)
            .build();

        coupon.setIsLimited(true);

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

    public static Authority getDummyAuthorityAsMember() {

        return Authority.builder()
            .id(1L)
            .name("member")
            .build();
    }

    public static Authority getDummyAuthorityAsAdmin() {

        return Authority.builder()
            .id(1L)
            .name("admin")
            .build();
    }

    public static MemberAuthority getDummyMemberAuthority() {
        Member member = getDummyMember();
        Authority authority = getDummyAuthorityAsAdmin();

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
            Order order = getDummyOrder();
            DeliveryStatusCode deliveryStatusCode = getDummyDeliveryStatusCode();
        DeliveryDetail deliveryDetail = DeliveryDetail.builder()
            .order(order)
            .orderNo(order.getId())
            .statusCode(deliveryStatusCode)
            .deliveryStatusCodeNo(deliveryStatusCode.getId())
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
            .memberNo(Dummy.getDummyMember().getMemberNo())
            .orderStatusCode(getDummyOrderStatusCode())
            .orderStatusCodeNo(getDummyOrderStatusCode().getId())
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

    public static CouponType getDummyCouponType() {
        return CouponType.builder()
            .id(1L)
            .name("정율")
            .build();
    }

    public static CouponCreateRequest getDummyCouponCreateRequest() {
        CouponCreateRequest couponRequest = new CouponCreateRequest();

        ReflectionTestUtils.setField(couponRequest, "name", "이달의 쿠폰");
        ReflectionTestUtils.setField(couponRequest, "imageId", 1L);
        ReflectionTestUtils.setField(couponRequest, "typeCode", 1L);
        ReflectionTestUtils.setField(couponRequest, "amount", 5);
        ReflectionTestUtils.setField(couponRequest, "isOrderCoupon", true);
        ReflectionTestUtils.setField(couponRequest, "applyItemId",
            Dummy.getDummyCategory().getId());
        ReflectionTestUtils.setField(couponRequest, "minimumUseAmount", 1000);
        ReflectionTestUtils.setField(couponRequest, "maximumDiscountAmount", 5000);
        ReflectionTestUtils.setField(couponRequest, "issuanceDeadlineAt", LocalDateTime.now());
        ReflectionTestUtils.setField(couponRequest, "isDuplicatable", true);
        ReflectionTestUtils.setField(couponRequest, "quantity", null);

        return couponRequest;
    }

    public static CouponTypeCURequest getDummyCouponTypeCURequest() {
        CouponTypeCURequest couponTypeRequest = new CouponTypeCURequest();
        ReflectionTestUtils.setField(couponTypeRequest, "id", 1L);
        ReflectionTestUtils.setField(couponTypeRequest, "name", "정율");

        return couponTypeRequest;
    }

    public static CouponUpdateRequest getDummyCouponUpdateRequest() {
        CouponUpdateRequest couponRequest = new CouponUpdateRequest();

        ReflectionTestUtils.setField(couponRequest, "name", "이달의 쿠폰");
        ReflectionTestUtils.setField(couponRequest, "typeCode", 1L);
        ReflectionTestUtils.setField(couponRequest, "amount", 5);
        ReflectionTestUtils.setField(couponRequest, "isOrderCoupon", false);
        ReflectionTestUtils.setField(couponRequest, "applyItemId", 1L);
        ReflectionTestUtils.setField(couponRequest, "minimumUseAmount", 1000);
        ReflectionTestUtils.setField(couponRequest, "maximumDiscountAmount", 5000);
        ReflectionTestUtils.setField(couponRequest, "issuanceDeadlineAt", LocalDateTime.now());
        ReflectionTestUtils.setField(couponRequest, "isDuplicatable", true);

        return couponRequest;
    }

    public static MemberCreateRequest getDummyMemberCreateRequest() {
        MemberCreateRequest memberRequest = new MemberCreateRequest();
        ReflectionTestUtils.setField(memberRequest, "gender", "M");
        ReflectionTestUtils.setField(memberRequest, "memberId", "HoDong");
        ReflectionTestUtils.setField(memberRequest, "password",
            "$2a$12$5KoVJnK1WF2h4h4T3FmifeO3ZLtAjiayJ783EfvTs7zSIz2GUhnMu");
        ReflectionTestUtils.setField(memberRequest, "nickname", "천하장사");
        ReflectionTestUtils.setField(memberRequest, "name", "강호동");
        ReflectionTestUtils.setField(memberRequest, "birthday", LocalDate.now());
        ReflectionTestUtils.setField(memberRequest, "phoneNo", "01012341234");
        ReflectionTestUtils.setField(memberRequest, "email", "aaaa@gmail.com");

        return memberRequest;
    }

    public static MemberUpdateRequest getDummyMemberUpdateRequest() {
        MemberUpdateRequest memberRequest = new MemberUpdateRequest();
        ReflectionTestUtils.setField(memberRequest, "gender", "M");
        ReflectionTestUtils.setField(memberRequest, "password",
            "$2a$12$5KoVJnK1WF2h4h4T3FmifeO3ZLtAjiayJ783EfvTs7zSIz2GUhnMu");
        ReflectionTestUtils.setField(memberRequest, "nickname", "천하장사123");
        ReflectionTestUtils.setField(memberRequest, "name", "강호동123");
        ReflectionTestUtils.setField(memberRequest, "birthday", LocalDate.now());
        ReflectionTestUtils.setField(memberRequest, "phoneNo", "01033333333");
        ReflectionTestUtils.setField(memberRequest, "email", "bbbb@gmail.com");

        return memberRequest;
    }

    public static DeliveryDestinationCURequest getDummyDeliveryDestinationCreateRequest() {
        return new DeliveryDestinationCURequest("집", "12345", "서울특별시 송파구 올림픽로 240", "", "", true);
    }

    public static CouponRetrieveResponse getDummyCouponRetrieveResponse() {
        return CouponRetrieveResponse.fromEntity(Dummy.getDummyCoupon());
    }

    public static OrderCoupon getDummyOrderCoupon() {
        OrderCoupon orderCoupon = OrderCoupon.builder()
            .coupon(getDummyCoupon())
            .code(UUID.randomUUID().toString().substring(0, 30))
            .build();

        ReflectionTestUtils.setField(orderCoupon, "id", 1L);

        return orderCoupon;
    }

    public static PointHistory getDummyPointHistory() {
        PointHistory pointHistory = PointHistory.builder()
            .member(getDummyMember())
            .point(100)
            .totalPoint(150)
            .updatedDetail("상품구매")
            .build();

        ReflectionTestUtils.setField(pointHistory, "id", 1L);
        ReflectionTestUtils.setField(pointHistory, "updatedAt", LocalDateTime.now());

        return pointHistory;
    }

    public static PointHistoryCreateRequest getDummyPointHistoryCreateRequest() {
        return PointHistoryCreateRequest.builder()
            .memberNo(getDummyMember().getMemberNo())
            .point(3000)
            .updatedDetail("test용")
            .build();
    }

    public static CartAddRequest getDummyGuestCartAddRequest() {
        CartAddRequest request = new CartAddRequest();
        ReflectionTestUtils.setField(request, "cartId", "290c59b9-5d20-433e-8e69-983e42d2b905");
        ReflectionTestUtils.setField(request, "productNo", 4L);
        ReflectionTestUtils.setField(request, "count", 3);
        return request;
    }

    public static CartAddRequest getDummyMemberCartAddRequest() {
        CartAddRequest request = new CartAddRequest();
        ReflectionTestUtils.setField(request, "cartId", "5");
        ReflectionTestUtils.setField(request, "productNo", 4L);
        ReflectionTestUtils.setField(request, "count", 3);
        return request;
    }

    public static BlockedMemberDetail getDummyBlockedMemberDetail() {
        BlockedMemberDetail blockedMemberDetail = BlockedMemberDetail.builder()
            .member(getDummyMember())
            .reason("test reason")
            .build();

        ReflectionTestUtils.setField(blockedMemberDetail, "id", 1L);
        ReflectionTestUtils.setField(blockedMemberDetail, "blockedAt", LocalDateTime.now());

        return blockedMemberDetail;
    }
}
