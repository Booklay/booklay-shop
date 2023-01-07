package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.Gender;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.PostType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

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

    return member;
  }

}
