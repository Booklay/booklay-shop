package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "object_file")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ObjectFile {

    @Id
    @Column(name = "file_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String fileAddress;

    @Column(name = "file_name")
    private String fileName;

    @Builder
    public ObjectFile(Long id, String fileAddress, String fileName) {
        this.id = id;
        this.fileAddress = fileAddress;
        this.fileName = fileName;
    }
}
