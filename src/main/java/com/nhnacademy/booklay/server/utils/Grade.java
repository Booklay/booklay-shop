package com.nhnacademy.booklay.server.utils;

import lombok.Getter;

@Getter
public enum Grade {
    ANY(0, "제한 없음"),
    WHITE(1, "화이트"),
    SILVER(2, "실버"),
    GOLD(3, "골드"),
    PLATINUM(4, "플래티넘");

    private final int value;
    private final String korGrade;

    Grade(int value, String korGrade) {
        this.value = value;
        this.korGrade = korGrade;
    }
}
