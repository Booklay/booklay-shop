package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id
    @Column(name = "authority_no")
    private Long id;

    @Column
    private String authority;

    @Builder
    public Authority(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }
}
