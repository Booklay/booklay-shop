package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "product")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_no")
    @Setter
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_no", updatable = false,  insertable = false)
    private ObjectFile objectFile;

    @Setter
    @Column(name = "thumbnail_no")
    private Long thumbnailNo;

    @Column
    private String title;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    @Column(name = "created_at")
    @Setter
    private LocalDateTime createdAt;

    @Column
    private Long price;

    @Column(name = "point_rate")
    private Long pointRate;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "is_selling")
    @Setter
    private boolean isSelling;

    @Column(name = "point_method")
    private boolean pointMethod;

    @Column(name = "is_deleted")
    @Setter
    private boolean isDeleted = false;

    @Builder
    public Product(Long thumbnailNo, String title, Long price, Long pointRate,
                   String shortDescription, String longDescription, boolean isSelling,
                   boolean pointMethod) {
        this.thumbnailNo = thumbnailNo;
        this.title = title;
        this.price = price;
        this.pointRate = pointRate;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.isSelling = isSelling;
        this.pointMethod = pointMethod;
    }


}
