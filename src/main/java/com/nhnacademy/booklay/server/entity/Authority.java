package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Authority {

    @Id
    @Column(name = "authority_no")
    private Long id;

    @Column
    private String authority;
}
