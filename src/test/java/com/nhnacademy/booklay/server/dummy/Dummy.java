package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponIssueRequest;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponRetrieveResponseFromProduct;
import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberAuthorityUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.PointPresentRequest;
import com.nhnacademy.booklay.server.dto.member.response.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberChartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeChartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberMainRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.OrderProductDto;
import com.nhnacademy.booklay.server.dto.order.payment.OrderReceipt;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.payment.StorageRequest;
import com.nhnacademy.booklay.server.dto.order.payment.SubscribeDto;
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
import com.nhnacademy.booklay.server.entity.OrderProduct;
import com.nhnacademy.booklay.server.entity.OrderStatusCode;
import com.nhnacademy.booklay.server.entity.PointHistory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
            .addressDetail("조선대학교")
            .addressSubDetail("(학동)")
            .receiver("홍길동")
            .receiverPhoneNo("010-1111-1111")
            .isDefaultDestination(true)
            .build();
        ReflectionTestUtils.setField(deliveryDestination, "id", 1L);
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
                .orderTitle("Dummy외 0건")
                .pointAccumulate(0)
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
        return new DeliveryDestinationCURequest("집", "12345", "서울특별시 송파구 올림픽로 240", "호수공원",
            "(올림픽로)", "심청이", "010-2222-2222", true);
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

    public static CouponIssueRequest getDummyCouponIssueRequest() {
        return new CouponIssueRequest(1L, 1L);
    }

    public static MemberLoginResponse getDummyMemberLoginResponse() {
        return new MemberLoginResponse(getDummyMember().getMemberId(),
            getDummyMember().getPassword(),
            "member",
            getDummyMember().getEmail(),
            getDummyMember().getIsBlocked()
        );
    }

    public static MemberRetrieveResponse getDummyMemberRetrieveResponse() {
        return MemberRetrieveResponse.builder()
            .memberNo(getDummyMember().getMemberNo())
            .gender(getDummyMember().getGender().getName())
            .memberId(getDummyMember().getMemberId())
            .nickname(getDummyMember().getNickname())
            .name(getDummyMember().getName())
            .birthday(getDummyMember().getBirthday())
            .phoneNo(getDummyMember().getPhoneNo())
            .email(getDummyMember().getEmail())
            .createdAt(getDummyMember().getCreatedAt())
            .updatedAt(getDummyMember().getUpdatedAt())
            .deletedAt(getDummyMember().getDeletedAt())
            .isBlocked(getDummyMember().getIsBlocked())
            .memberGrade("화이트")
            .authority("member")
            .build();
    }

    public static MemberAuthorityRetrieveResponse getDummyMemberAuthorityRetrieveResponse() {
        return MemberAuthorityRetrieveResponse.builder()
            .id(getDummyMemberAuthority().getAuthority().getId())
            .name(getDummyMemberAuthority().getAuthority().getName())
            .build();
    }

    public static MemberMainRetrieveResponse getDummyMemberMainRetrieveResponse() {
        return MemberMainRetrieveResponse.builder()
            .memberNo(getDummyMember().getMemberNo())
            .gender(getDummyMember().getGender().getName())
            .memberId(getDummyMember().getMemberId())
            .nickname(getDummyMember().getNickname())
            .name(getDummyMember().getName())
            .birthday(getDummyMember().getBirthday())
            .phoneNo(getDummyMember().getPhoneNo())
            .email(getDummyMember().getEmail())
            .memberGrade("화이트")
            .currentTotalPoint(1000)
            .build();
    }

    public static DeliveryDestinationRetrieveResponse getDummyDeliveryDestinationRetrieveResponse() {
        return DeliveryDestinationRetrieveResponse.builder()
            .id(getDummyDeliveryDestination().getId())
            .memberNo(getDummyDeliveryDestination().getMember().getMemberNo())
            .name(getDummyDeliveryDestination().getName())
            .zipCode(getDummyDeliveryDestination().getZipCode())
            .address(getDummyDeliveryDestination().getAddress())
            .addressDetail(getDummyDeliveryDestination().getAddressDetail())
            .addressSubDetail(getDummyDeliveryDestination().getAddressSubDetail())
            .receiver(getDummyDeliveryDestination().getReceiver())
            .receiverPhoneNo(getDummyDeliveryDestination().getReceiverPhoneNo())
            .isDefaultDestination(getDummyDeliveryDestination().getIsDefaultDestination())
            .build();
    }

    public static TotalPointRetrieveResponse getDummyTotalPointRetrieveResponse() {
        return TotalPointRetrieveResponse.builder()
            .member(getDummyMember().getMemberNo())
            .totalPoint(10000)
            .build();
    }

    public static PointPresentRequest getDummyPointPresentRequest() {
        return PointPresentRequest.builder()
            .targetMemberId(getDummyMember().getMemberId())
            .targetPoint(1000)
            .build();
    }

    public static MemberChartRetrieveResponse getDummyMemberChartRetrieveResponse() {
        return MemberChartRetrieveResponse.builder()
            .validMemberCount(1L)
            .blockedMemberCount(1L)
            .droppedMemberCount(1L)
            .build();
    }

    public static MemberGradeChartRetrieveResponse getDummyMemberGradeChartRetrieveResponse() {
        return MemberGradeChartRetrieveResponse.builder()
            .whiteCount(1L)
            .silverCount(1L)
            .goldCount(1L)
            .platinumCount(1L)
            .build();
    }

    public static MemberBlockRequest getDummyMemberBlockRequest() {
        return MemberBlockRequest.builder()
            .reason("test")
            .build();
    }

    public static MemberAuthorityUpdateRequest getDummyMemberAuthorityUpdateRequest() {
        return new MemberAuthorityUpdateRequest("실버");
    }

    public static MemberGradeRetrieveResponse getDummyMemberGradeRetrieveResponse() {
        return MemberGradeRetrieveResponse.builder()
            .id(getDummyMemberGrade().getId())
            .memberNo(getDummyMemberGrade().getMember().getMemberNo())
            .name(getDummyMemberGrade().getName())
            .date(getDummyMemberGrade().getDate())
            .build();
    }

    public static BlockedMemberRetrieveResponse getDummyBlockedMemberRetrieveResponse() {
        return BlockedMemberRetrieveResponse.builder()
            .id(1L)
            .memberNo(getDummyBlockedMemberDetail().getMember().getMemberNo())
            .memberId(getDummyBlockedMemberDetail().getMember().getMemberId())
            .name(getDummyBlockedMemberDetail().getMember().getName())
            .reason(getDummyBlockedMemberDetail().getReason())
            .blockedAt(getDummyBlockedMemberDetail().getBlockedAt())
            .releasedAt(getDummyBlockedMemberDetail().getReleasedAt())
            .build();
    }

    public static DroppedMemberRetrieveResponse getDummyDroppedMemberRetrieveResponse() {
        return DroppedMemberRetrieveResponse.builder()
            .memberId(getDummyMember().getMemberId())
            .deletedAt(LocalDateTime.now())
            .build();
    }

    public static PointHistoryRetrieveResponse getDummyPointHistoryRetrieveResponse() {
        return PointHistoryRetrieveResponse.builder()
            .id(getDummyPointHistory().getId())
            .member(getDummyPointHistory().getMember().getMemberNo())
            .point(getDummyPointHistory().getPoint())
            .totalPoint(getDummyPointHistory().getTotalPoint())
            .updatedAt(getDummyPointHistory().getUpdatedAt())
            .updatedDetail(getDummyPointHistory().getUpdatedDetail())
            .build();
    }

    public static OrderReceipt getDummyOrderReceipt() {
        return new OrderReceipt(getDummyOrder());
    }

    public static OrderListRetrieveResponse getDummyOrderListRetrieveResponse() {
        return new OrderListRetrieveResponse(getDummyOrder());
    }

    public static StorageRequest getDummyStorageRequest() {
        return new StorageRequest(List.of(new CartDto(getDummyOrderProduct().getProductNo(), 1)));
    }

    public static OrderSheet getDummyOrderSheet() {
        OrderSheet orderSheet = new OrderSheet();
//        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("couponCodeList"));
        ReflectionTestUtils.setField(orderSheet, "cartDtoList", List.of(new CartDto(1L, 1)));
//        ReflectionTestUtils.setField(orderSheet, "orderProductDtoList", List.of(new OrderProductDto(1L, "test", 1, 1000)));
//        ReflectionTestUtils.setField(orderSheet, "subscribeProductList", List.of(new SubscribeDto(1L, 1, 1000)));
//        ReflectionTestUtils.setField(orderSheet, "couponUseRequest", List.of("couponCodeList"));
        ReflectionTestUtils.setField(orderSheet, "productPriceSum", 5000L);
        ReflectionTestUtils.setField(orderSheet, "usingPoint", 1000L);
        ReflectionTestUtils.setField(orderSheet, "deliveryPrice", 3000L);
        ReflectionTestUtils.setField(orderSheet, "giftWrappingPrice", 3000L);
        ReflectionTestUtils.setField(orderSheet, "discountPrice", 1000L);
        ReflectionTestUtils.setField(orderSheet, "paymentAmount", 10000L);
        ReflectionTestUtils.setField(orderSheet, "paymentMethod", 1L);
        ReflectionTestUtils.setField(orderSheet, "orderId", "testOrder");
        ReflectionTestUtils.setField(orderSheet, "memberNo", 1L);
        ReflectionTestUtils.setField(orderSheet, "sender", "testSender");
        ReflectionTestUtils.setField(orderSheet, "senderPhoneNo", "01012341234");
        ReflectionTestUtils.setField(orderSheet, "name", "testName");
        ReflectionTestUtils.setField(orderSheet, "zipCode", "00000");
        ReflectionTestUtils.setField(orderSheet, "address", "testAddress");
        ReflectionTestUtils.setField(orderSheet, "isDefaultDestination", true);
        ReflectionTestUtils.setField(orderSheet, "receiver", "testReceiver");
        ReflectionTestUtils.setField(orderSheet, "receiverPhoneNo", "01012341234");
        ReflectionTestUtils.setField(orderSheet, "memo", "testMemo");
//        ReflectionTestUtils.setField(orderSheet, "orderNo", 1L);
        ReflectionTestUtils.setField(orderSheet, "orderTitle", "testOrder");
//        ReflectionTestUtils.setField(orderSheet, "pointAccumulate", List.of("couponCodeList"));

        return orderSheet;
    }

    public static MemberInfo getDummyMemberInfo() {
        String[] longStringArray = new String[] {"1"};
        String[] stringArray = new String[] {"test"};
        String[] dateArray = new String[] {"2000, 01, 01"};
        MemberInfo memberInfo= new MemberInfo();

        ReflectionTestUtils.setField(memberInfo, "memberNo", 1L);
        ReflectionTestUtils.setField(memberInfo, "gender" , "여자");
        ReflectionTestUtils.setField(memberInfo, "memberId", "testMemberId");
        ReflectionTestUtils.setField(memberInfo, "nickname", "testNickname");
        ReflectionTestUtils.setField(memberInfo, "name", "testName");
        ReflectionTestUtils.setField(memberInfo, "birthday", LocalDate.of(2000, 2, 9));
        ReflectionTestUtils.setField(memberInfo, "phoneNo", "01012341234");
        ReflectionTestUtils.setField(memberInfo, "email", "test@test.com");

        return memberInfo;
    }

    public static CouponRetrieveResponseFromProduct getDummyCouponRetrieveResponseFromProduct() {
        CouponRetrieveResponseFromProduct response =
            new CouponRetrieveResponseFromProduct();

        ReflectionTestUtils.setField(response, "id", 1L);
        ReflectionTestUtils.setField(response, "name", "이달의 쿠폰");
        ReflectionTestUtils.setField(response, "typeName", "정액");
        ReflectionTestUtils.setField(response, "amount", 5000);
        ReflectionTestUtils.setField(response, "minimumUseAmount", 1000);
        ReflectionTestUtils.setField(response, "maximumDiscountAmount", 5000);
        ReflectionTestUtils.setField(response, "isLimited", true);
        ReflectionTestUtils.setField(response, "couponCode", "testCouponCode");
        ReflectionTestUtils.setField(response, "categoryNo", 1L);
        ReflectionTestUtils.setField(response, "productNo", 1L);

        return response;
    }
}
