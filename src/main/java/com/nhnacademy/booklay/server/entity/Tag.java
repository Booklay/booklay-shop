package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @Column(name = "tag_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tag;

    @Builder
    public Tag(Long id, String tag) {
        this.id = id;
        this.tag = tag;
    }
}
