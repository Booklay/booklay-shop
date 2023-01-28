package com.nhnacademy.booklay.server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "product_author")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProductAuthor {

    @EmbeddedId
    private Pk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_no")
    @MapsId("bookId")
    private ProductDetail productDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_no")
    @MapsId("authorId")
    private Author author;

    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {

        @Column(name = "book_no")
        private Long bookId;

        @Column(name = "author_no")
        private Long authorId;
    }

    @Builder
    public ProductAuthor(Pk pk, ProductDetail productDetail, Author author) {
        this.pk = pk;
        this.productDetail = productDetail;
        this.author = author;
    }
}
