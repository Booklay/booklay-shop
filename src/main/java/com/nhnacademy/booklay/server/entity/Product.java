package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name="product")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

  @CreatedDate
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

  @Column(name = "registed_at")
  private LocalDateTime registedAt;

  @Column
  private Long price;

  @Column(name="point_rate")
  private Long pointRate;

  @Column(name="short_description")
  private String shortDescription;

  @Column(name="long_description")
  private String longDescription;

  @Column(name="is_selling")
  private boolean isSelling;

  @Column(name="point_method")
  private boolean pointMethod;

  @Builder
  public Product(Image image, String title, Long price, Long pointRate, String shortDescription, String longDescription, boolean isSelling, boolean pointMethod) {
    this.image = image;
    this.title = title;
    this.price = price;
    this.pointRate = pointRate;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.isSelling = isSelling;
    this.pointMethod = pointMethod;
  }
}
