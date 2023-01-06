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
public class Image {

    @Id
    @Column(name = "image_no")
    private Long id;

    @Column
    private String address;

    @Column
    private String ext;

    @Builder
    public Image(Long id, String address, String ext) {
        this.id = id;
        this.address = address;
        this.ext = ext;
    }
}
