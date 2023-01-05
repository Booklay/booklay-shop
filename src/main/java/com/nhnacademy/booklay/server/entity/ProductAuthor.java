package com.nhnacademy.booklay.server.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_author")
public class ProductAuthor {

  @EmbeddedId
  private ProductAuthorPK id;

  //요 밑으로 옵니까?


  @Embeddable
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public class ProductAuthorPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "book_no")
    private ProductDetail productDetail;

    @ManyToOne
    @JoinColumn(name = "author_no")
    private Author author;
  }
}
