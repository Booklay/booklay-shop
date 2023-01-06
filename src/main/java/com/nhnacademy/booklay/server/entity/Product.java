package com.nhnacademy.booklay.server.entity;

import lombok.*;

import java.time.LocalDateTime;
import javax.persistence.*;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_no")
  private Long id;

  @OneToOne
  @JoinColumn(name = "thumbnail_no")
  private Image image;

  @Column
  private String title;

  @Column(name="registed_at")
  private LocalDateTime registedAt;

  @Column
  private int price;

  @Column(name="point_rate")
  private int pointRate;

  @Column(name="short_description")
  private String shortDescription;

  @Column(name="long_description")
  private String longDescription;

  @Column(name="is_selling")
  private boolean isSelling;

  @Column(name="point_method")
  private boolean pointMethod;

  @Builder
  public Product(Long id, Image image, String title, LocalDateTime registedAt, int price, int pointRate, String shortDescription, String longDescription, boolean isSelling, boolean pointMethod) {
    this.id = id;
    this.image = image;
    this.title = title;
    this.registedAt = registedAt;
    this.price = price;
    this.pointRate = pointRate;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.isSelling = isSelling;
    this.pointMethod = pointMethod;
  }
}
