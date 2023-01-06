package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "gender")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gender {

    @Id
    @Column(name = "gender_no")
    private Long id;

    @Column(name = "gender")
    private String gender;

    @Builder
    public Gender(Long id, String gender) {
        this.id = id;
        this.gender = gender;
    }
}
