package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "category_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryProduct {

  @EmbeddedId
  private Pk pk;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_no")
  @MapsId("categoryId")
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_no")
  @MapsId("productId")
  private Product product;


  @Embeddable
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Pk implements Serializable {
    @Column(name = "category_no")
    private Long categoryId;

    @Column(name = "product_no")
    private Long productId;
  }

  @Builder
  public CategoryProduct(Pk pk, Category category, Product product) {
    this.pk = pk;
    this.category = category;
    this.product = product;
  }
}
