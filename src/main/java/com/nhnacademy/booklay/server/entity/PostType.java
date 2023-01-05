package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "post_type")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostType {

    @Id
    @Column(name = "post_type_no")
    private Long id;

    @Column
    private String type;
}
