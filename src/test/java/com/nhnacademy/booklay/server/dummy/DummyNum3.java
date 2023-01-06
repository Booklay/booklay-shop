package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.OwnedEbook;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class DummyNum3 {


    private static Product getDummyProductForDummy(){
        Image image = Image.builder()
            .address("")
            .ext("")
            .build();
        ReflectionTestUtils.setField(image, "id", 1L);
        Product product = Product.builder()
            .image(image)
            .price(1)
            .pointRate(1)
            .isSelling(true)
            .longDescription("")
            .pointMethod(true)
            .registedAt(LocalDateTime.now())
            .title("")
            .shortDescription("")
            .build();
        return product;
    }

    private static Member getDummyMemberForDummy(){
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
        return member;
    }
    public static RestockingNotification getDummyRestockingNotification() {

        RestockingNotification restockingNotification = RestockingNotification.builder()
            .product(getDummyProductForDummy())
            .member(getDummyMemberForDummy())
            .build();

        ReflectionTestUtils.setField(restockingNotification, "pk", new RestockingNotification.Pk(1L, 1L));

        return restockingNotification;
    }

    public static OwnedEbook getDummyOwnedEbook() {
        OwnedEbook ownedEbook = OwnedEbook.builder()
            .product(getDummyProductForDummy())
            .member(getDummyMemberForDummy())
            .build();

        ReflectionTestUtils.setField(ownedEbook, "id", 1L);

        return ownedEbook;
    }
}
