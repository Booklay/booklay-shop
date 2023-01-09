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
public class Authority {

    @Id
    @Column(name = "authority_no")
    private Long id;

    @Column
    private String name;

    @Builder
    public Authority(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
