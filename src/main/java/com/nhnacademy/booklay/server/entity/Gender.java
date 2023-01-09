package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Table(name = "gender")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Gender {

    @Id
    @Column(name = "gender_no")
    private Long id;

    @Column(name = "gender")
    private String name;

    @Builder
    public Gender(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
