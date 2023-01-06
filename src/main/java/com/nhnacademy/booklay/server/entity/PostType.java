package com.nhnacademy.booklay.server.entity;

import javax.persistence.EntityListeners;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "post_type")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PostType {

    @Id
    @Column(name = "post_type_no")
    private Long id;

    @Column
    private String type;

    @Builder
    public PostType(Long id, String type) {
        this.id = id;
        this.type = type;
    }
}
