package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Tag {

    @Id
    @Column(name = "tag_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @Column(name = "tag")
    private String name;

    @Builder
    public Tag(String name) {
        this.name = name;
    }
}
