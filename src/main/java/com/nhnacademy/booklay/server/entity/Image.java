package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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
