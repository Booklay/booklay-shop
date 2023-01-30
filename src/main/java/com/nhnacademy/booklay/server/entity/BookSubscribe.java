package com.nhnacademy.booklay.server.entity;

import com.nhnacademy.booklay.server.entity.CategoryProduct.Pk;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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

@Entity
@Table(name = "book_subscribe")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookSubscribe {
  @EmbeddedId
  private Pk pk;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_no")
  @MapsId("productId")
  private Product productNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subscribe_no")
  @MapsId("subscribeId")
  private Subscribe subscribeNo;

  @Column(name= "release_Date")
  private LocalDate releaseDate;

  @Embeddable
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Pk implements Serializable {
    @Column(name = "subscribe_no")
    private Long subscribeId;

    @Column(name = "product_no")
    private Long productId;
  }

  @Builder
  public BookSubscribe(Pk pk, Product productNo, Subscribe subscribeNo, LocalDate releaseDate) {
    this.pk = pk;
    this.productNo = productNo;
    this.subscribeNo = subscribeNo;
    this.releaseDate = releaseDate;
  }
}
