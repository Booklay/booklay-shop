package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "post_type")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PostType {

    @Id
    @Column(name = "post_type_no")
    private Long postTypeId;

    @Column
    private String type;

    @Builder
    public PostType(Long postTypeId, String type) {
        this.postTypeId = postTypeId;
        this.type = type;
    }
}
