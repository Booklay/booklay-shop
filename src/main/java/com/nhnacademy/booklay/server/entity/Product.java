package com.nhnacademy.booklay.server.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

  @Id
  @Column(name = "product_no")
  private Long id;

  @Column(name="thumbnail_no")
  private Long thumbnailId;

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
}
