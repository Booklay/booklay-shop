package com.nhnacademy.booklay.server.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.*;

@Table(name = "product_author")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAuthor {

  @EmbeddedId
  private Pk pk;

  @ManyToOne
  @JoinColumn(name = "book_no")
  @MapsId("bookId")
  private ProductDetail productDetail;

  @ManyToOne
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
}
