package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "coupon_birthday_setting")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponBirthdaySetting {
    @Id
    @Column(name = "setting_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long settingNo;
    @Column(name = "coupon_template_no")
    Long couponTemplateNo;
    @Column(name = "member_grade")
    Long memberGrade;
}
