package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

public class DummyNum3 {


    private static Product getDummyProductForDummy() {
        ObjectFile objectFile = ObjectFile.builder()
            .fileAddress("")
            .fileName("")
            .build();
        ReflectionTestUtils.setField(objectFile, "id", 1L);
        Product product = Product.builder()
            .objectFile(objectFile)
            .price(1L)
            .pointRate(1L)
            .isSelling(true)
            .longDescription("")
            .pointMethod(true)
            .title("")
            .shortDescription("")
            .build();
        return product;
    }

    private static Member getDummyMemberForDummy() {
        Gender gender = Gender.builder()
            .id(1L)
            .name("M")
            .build();

        Member member = Member.builder()
            .gender(gender)
            .memberId("dummyMemberId")
            .password("$2a$12$5KoVJnK1WF2h4h4T3FmifeO3ZLtAjiayJ783EfvTs7zSIz2GUhnMu") //1234
            .nickname("유재석")
            .name("강호동")
            .birthday(LocalDate.now())
            .phoneNo("01012341234")
            .email("www.abcd.com")
            .isBlocked(false)
            .build();
        return member;
    }

    public static RestockingNotification getDummyRestockingNotification() {

        RestockingNotification restockingNotification = RestockingNotification.builder()
            .product(DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto()))
            .member(getDummyMemberForDummy())
            .build();

        ReflectionTestUtils.setField(restockingNotification, "pk",
            new RestockingNotification.Pk(1L, 1L));

        return restockingNotification;
    }

    public static OwnedEbook getDummyOwnedEbook() {
        OwnedEbook ownedEbook = OwnedEbook.builder()
            .product(DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto()))
            .member(getDummyMemberForDummy())
            .build();

        ReflectionTestUtils.setField(ownedEbook, "id", 1L);

        return ownedEbook;
    }
}
