package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.entity.OrderProduct;
import com.nhnacademy.booklay.server.entity.Product;
import org.aspectj.weaver.ast.Or;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Dummy {

    /**
     * auto increment 있는 엔티티의 경우 전역변수 추가하여 사용
     * <p>
     * 사용시 최상위 엔티티만 get 하여 사용한다.
     * <p>
     * ex) DeliveryDetail 사용시 order 와 member 더미가 필요함.
     * - DeliveryDetail detail = getDummyDeliveryDetail()
     * detail.getOrder()
     * detail.getOrder().getMember()
     * 와 같은 형태로 사용
     */

    private static long memberId = 0;
    private static long orderId = 0;
    private static long memberGradeId = 0;
    private static long deliveryDetailId = 0;
    private static long orderProductId = 0;
    private static long couponId = 0;

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

        ReflectionTestUtils.setField(member, "memberId", ++memberId);

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

        ReflectionTestUtils.setField(coupon, "id", ++couponId);

        return coupon;
    }

    public static OrderProduct getDummyOrderProduct() {
        OrderProduct orderProduct = OrderProduct.builder()
            .order(null)
            .product(null)
            .count(1)
            .price(10000)
            .build();

        ReflectionTestUtils.setField(orderProduct, "id", ++orderProductId);

        return orderProduct;

    }
    public static Authority getDummyAuthority() {
        Authority authority = Authority.builder()
            .id(1L)
            .authority("admin")
            .build();

        return authority;
    }

    public static MemberAuthority getDummyMemberAuthority() {
        Member member = getDummyMember();
        Authority authority = getDummyAuthority();

        MemberAuthority memberAuthority = MemberAuthority.builder()
            .pk(new MemberAuthority.Pk(member.getMemberId(), authority.getId()))
            .member(member)
            .authority(authority)
            .build();

        return memberAuthority;
    }

    public static MemberGrade getDummyMemberGrade() {
        MemberGrade memberGrade = MemberGrade.builder()
            .member(getDummyMember())
            .name("white")
            .build();

        ReflectionTestUtils.setField(memberGrade, "id", ++memberGradeId);

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
            .depth(2L)
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

        ReflectionTestUtils.setField(deliveryDetail, "id", ++deliveryDetailId);
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

        ReflectionTestUtils.setField(order, "id", ++orderId);
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
